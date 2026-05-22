package com.axity.dinosaurpark.events;

import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.Random;

import com.axity.dinosaurpark.persistence.EventRecord;
import com.axity.dinosaurpark.simulation.ParkState;

public class DealHourEvent implements SimulationEvent{

    private final double probability;

    public DealHourEvent(double probability){
        this.probability = probability;
    }

    @Override
    public String getName() {
        return "HORA_DE_OFERTAS";
    }

    @Override
    public String getDescription() {
        return "Hora de las ofertas";
    }

    @Override
    public void execute(ParkState state, Random rng) {
        System.out.println("-------------------------------------");
        System.out.println("EVENTO EN EJECUCION "+getName());
        System.out.println("-------------------------------------");
        state.setEventToAllEventsPerDays(getName());
        if (rng.nextDouble() < getProbability()) {
            state.setDealHourActive(true);
            state.setCurrentDiscount(0.30);
        }
    }

    @Override
    public EventRecord toRecord(long step) throws SQLException {
        return new EventRecord(0L, step, getName(), getDescription(), "Tourist, Zones", LocalDateTime.now());
    }

    @Override
    public double getProbability() {
        return probability;
    }

}
