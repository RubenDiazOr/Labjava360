package com.axity.dinosaurpark.events;

import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.Random;

import com.axity.dinosaurpark.persistence.EventRecord;
import com.axity.dinosaurpark.simulation.ParkState;
import com.axity.dinosaurpark.zone.PowerPlant;

public class BlackoutEvent implements SimulationEvent {

    private final double probability;
    
    public BlackoutEvent(double probability) {
        this.probability = probability;
    }

    @Override
    public String getName() {
        return "APAGON_MASIVO";
    }


    @Override
    public String getDescription() {
        return "Apagón masivo en Dinosaur Park";
    }

    @Override
    public void execute(ParkState state, Random rng) {
        System.out.println("-------------------------------------");
        System.out.println("EVENTO EN EJECUCION "+getName());
        System.out.println("-------------------------------------");
        state.setEventToAllEventsPerDays(getName());
        PowerPlant powerPlant = (PowerPlant) state.getZones().get("PowerPlant");
        try {
            powerPlant.triggerFailure(state.getDb());
            state.updateTotalExpenses(state.getConfig().getDouble("powerplant.repairCost", 200.0));
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public EventRecord toRecord(long step) {
        return new EventRecord(0L, step, getName(), getDescription(), "PowerPlant, Technician, Vehicule", LocalDateTime.now());
    }

    @Override
    public double getProbability() {
        return probability;
    }

}
