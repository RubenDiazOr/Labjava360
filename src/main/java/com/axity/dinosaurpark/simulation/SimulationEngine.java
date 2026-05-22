package com.axity.dinosaurpark.simulation;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import com.axity.dinosaurpark.events.BlackoutEvent;
import com.axity.dinosaurpark.events.DealHourEvent;
import com.axity.dinosaurpark.events.DinosaurEscapeEvent;
import com.axity.dinosaurpark.events.SimulationEvent;
import com.axity.dinosaurpark.events.StormEvent;
import com.axity.dinosaurpark.events.VehiculeFailureEvent;
import com.axity.dinosaurpark.model.CarnivoreDinosaur;
import com.axity.dinosaurpark.model.Dinosaur;
import com.axity.dinosaurpark.model.DinosaurStatus;
import com.axity.dinosaurpark.model.Guard;
import com.axity.dinosaurpark.model.HerbivoreDinosaur;
import com.axity.dinosaurpark.model.Technician;
import com.axity.dinosaurpark.model.Tourist;
import com.axity.dinosaurpark.model.TouristStatus;
import com.axity.dinosaurpark.model.Vehicule;
import com.axity.dinosaurpark.model.Worker;
import com.axity.dinosaurpark.monitoring.ParkMonitor;
import com.axity.dinosaurpark.persistence.DatabaseService;
import com.axity.dinosaurpark.zone.ArrivalZone;
import com.axity.dinosaurpark.zone.BathroomZone;
import com.axity.dinosaurpark.zone.CentralHub;
import com.axity.dinosaurpark.zone.ObservationEnclosure;
import com.axity.dinosaurpark.zone.ParkZone;
import com.axity.dinosaurpark.zone.PowerPlant;

public class SimulationEngine implements Runnable {
    private final int totalSteps;
    private final int batchSize;
    private final Random rng;
    private final ArrivalZone arrivalZone;
    private final ParkState state;
    private final DatabaseService db;
    private final CentralHub centralHub;
    private final ObservationEnclosure enclosure;
    private final BathroomZone bathroomZone;
    private final PowerPlant powerPlant;
    private final Technician technician;
    private final int monitoringInterval;
    private List<SimulationEvent> allEvents;

    public SimulationEngine(ParkState state) throws Exception {
        this.state = state;
        this.batchSize = state.getConfig().getInt("simulation.arrivalBatchSize", 6);
        this.totalSteps = state.getConfig().getInt("simulation.totalSteps", 100);
        this.arrivalZone = new ArrivalZone(state);
        this.enclosure = new ObservationEnclosure(state);
        this.centralHub = new CentralHub(state);
        this.bathroomZone = new BathroomZone(state);
        this.db = new DatabaseService(state.getConfig().getString("db.path", "none"));
        this.powerPlant = new PowerPlant(state);
        this.rng = new Random();
        this.technician = new Technician(1, "Pedro", state.getConfig().getDouble("workers.dailySalary", 200));
        this.monitoringInterval = state.getConfig().getInt("monitoring.intervalSteps", 15);
        this.allEvents = new ArrayList<>();
    }

    public void initializeEvents() {
        allEvents = List.of(
            new BlackoutEvent(state.getConfig().getDouble("event.blackout.probability", 0.3)), 
            new StormEvent(state.getConfig().getDouble("event.storm.probability", 0.4)), 
            new DinosaurEscapeEvent(state.getConfig().getDouble("event.escape.probability", 0.6)),
            new DealHourEvent(state.getConfig().getDouble("event.deals.probability", 0.5)),
            new VehiculeFailureEvent(state.getConfig().getDouble("event.vehicleFailure.probability", 0.6))
        );
    }

    public void initializeZones() {
        Map<String, ParkZone> zones = new HashMap<>();
        zones.put("PowerPlant", powerPlant);
        zones.put("ArrivalZone", arrivalZone);
        zones.put("BathroomZone", bathroomZone);
        zones.put("CentralHubZone", centralHub);
        zones.put("EnclousureZone", enclosure);
        state.setZones(zones);
    }

    private void initializedWorkers() {
        List<Worker> workers = new ArrayList<>();
        for (int i = 0; i < state.getConfig().getInt("workers.guards", 5); i++) {
            workers.add(new Guard(i, "WGuard" + i, state.getConfig().getDouble("workers.dailySalary", 200)));
        }

        for (int i = 0; i < state.getConfig().getInt("workers.technicians", 5); i++) {
            workers.add(new Technician(i + 10, "WTechnician" + i, state.getConfig().getDouble("workers.dailySalary", 200)));
        }
        state.setWorkers(workers);
    }

    private void initializedDinosaurs() {
        List<Dinosaur> dinosaurs = new ArrayList<>();
        for (int i = 0; i < state.getConfig().getInt("dinosaurs.carnivores", 5); i++) {
            dinosaurs.add(
                    new CarnivoreDinosaur(i + 20, "Carnivor" + i, "Tiranosaurio", DinosaurStatus.IN_ENCLOSURE, 20));
        }
        for (int i = 0; i < state.getConfig().getInt("dinosaurs.herbivores", 5); i++) {
            dinosaurs.add(
                    new HerbivoreDinosaur(i + 30, "Herbivore" + i, "Cuello largo", DinosaurStatus.IN_ENCLOSURE, 10));
        }
        state.setDinosaurs(dinosaurs);
    }

    public void initializeVehicules() {
        List<Vehicule> vehicules = new ArrayList<>();
        for (int i = 0; i < state.getConfig().getInt("vehicules", 10); i++) {
            vehicules.add(new Vehicule(state.getConfig()));
        }
        state.setVehicules(vehicules);
    }

    @Override
    public void run() {
        initializeEvents();
        initializeZones();
        initializedDinosaurs();
        initializedWorkers();
        initializeVehicules();
        state.setDb(db);

        for (int step = 0; step < totalSteps; step++) {
            state.incrementStep();

            // A. LLEGADAS
            List<Tourist> arrived = arrivalZone.processBatch(batchSize, db, state.getCurrentDiscount());
            // registra ingresos por boletos
            state.setTourists(arrived);

            // B. MOVIMIENTO DE TURISTAS (todos los que están IN_PARK)
            for (Tourist t : arrived) {
                centralHub.visit(t, rng, db, state.getCurrentDiscount());
                bathroomZone.tryEnter(t, rng, db);
                enclosure.visit(t, rng, db);
                // encierro según id del turista
            }

            // C. TICKS DE ZONAS
            bathroomZone.tick();
            try {
                powerPlant.tick(rng, db);
            } catch (SQLException e) {
                e.printStackTrace();
            }

            // D. Limpiar eventos del step anterior
            state.clearActiveEvents();

            // Luego disparar eventos probabilísticos (reemplaza el scheduler)
            for (SimulationEvent event : allEvents) {
                if (state.getRng().nextDouble() < event.getProbability()) {
                    event.execute(state, state.getRng());
                    try {
                        db.appendEvent(event.toRecord(step));
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                    state.addActiveEvent(event.getName());
                }
            }



            // C. También hacer tick a los vehículos
            for (Vehicule v : state.getVehicules()) v.tick();

            // E. Technician ahora también necesita vehicles
            technician.repairIfNeeded(powerPlant, state.getVehicules());

            // F. Monitoreo CONDICIONAL — no cada step
            if (state.getCurrentStep() % monitoringInterval == 0)
                ParkMonitor.displaySnapshot(state);

        }

        state.getTourists().forEach(t -> {
            t.setStatus(TouristStatus.EXITED);
        });
        resumenFinal();
    }

    private void resumenFinal(){
        System.out.println("=====================================");
        System.out.println("       RESUMEN GENERAL DEL DIA     ");
        System.out.println("=====================================");
        System.out.println("____________________________________");
        System.out.printf("| RESUMEN FINAL - Step %-11d |%n", state.getCurrentStep());
        System.out.printf("| 1. Gastos operativos: $ %-9s|%n", state.getTotalExpenses());
        System.out.printf("| 2. Ventas realizadas: $ %-9s|%n", state.getSales());
        System.out.printf("| 3. Ganancias del dia: $ %-9s|%n", state.getTotalRevenue());
        System.out.printf("| 4. Eventos generados:  %-10d|%n", state.getAllEventsPerDay().size());
        System.out.println("|__________________________________|");
    }

}
