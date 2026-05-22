package com.axity.dinosaurpark.events;

import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Random;

import com.axity.dinosaurpark.model.Vehicule;
import com.axity.dinosaurpark.model.VehiculeStatus;
import com.axity.dinosaurpark.persistence.EventRecord;
import com.axity.dinosaurpark.simulation.ParkState;

public class VehiculeFailureEvent implements SimulationEvent{

    private final double probability;

    public VehiculeFailureEvent(double pro){
        this.probability = pro;
    }

    @Override
    public String getName() {
        return "FALLA_VEHICULE";
    }

    @Override
    public String getDescription() {
        return "Vehiculos dañados";
    }

    @Override
    public void execute(ParkState state, Random rng) {
        System.out.println("----------------------------------------");
        System.out.println("EVENTO EN EJECUCION "+getName());
        System.out.println("----------------------------------------");
        state.setEventToAllEventsPerDays(getName());
        if (rng.nextDouble() < getProbability()) {
            List<Vehicule> avaliableVehicules = state.getVehicules().stream().filter(v -> v.getStatus().equals(VehiculeStatus.AVAILABLE)).toList();
            if (avaliableVehicules.size() > 0) {
                Vehicule vehi = avaliableVehicules.get(rng.nextInt(avaliableVehicules.size()));  
                state.getVehicules().get(state.getVehicules().indexOf(vehi)).markBroken();
            }
        }
    }

    @Override
    public EventRecord toRecord(long step) throws SQLException {
        return new EventRecord(0L, step, getName(), getDescription(), "Vehicules, PowerPlant, Technicians", LocalDateTime.now());
    }

    @Override
    public double getProbability() {
        return probability;
    }

}
