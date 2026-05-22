package com.axity.dinosaurpark.events;

import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Random;

import com.axity.dinosaurpark.model.Tourist;
import com.axity.dinosaurpark.model.TouristStatus;
import com.axity.dinosaurpark.persistence.EventRecord;
import com.axity.dinosaurpark.persistence.ExpenseRecord;
import com.axity.dinosaurpark.simulation.ParkState;

public class StormEvent implements SimulationEvent{

    private final double probability;

    public StormEvent(double probability){
        this.probability = probability;
    }
    
    @Override
    public String getName() {
        return "TORMENTA_TORRENCIAL";
    }

    @Override
    public String getDescription() {
        return "Tormenta torrencial, evacuar";
    }

    @Override
    public void execute(ParkState state, Random rng) {
        System.out.println("---------------------------------------");
        System.out.println("EVENTO EN EJECUCION "+getName());
        System.out.println("---------------------------------------");
        state.setEventToAllEventsPerDays(getName());
        List<Tourist> touristInPark = state.getTourists().stream().filter(t -> t.getStatus().equals(TouristStatus.IN_PARK)).toList();
        touristInPark.forEach(t -> {
            t.recordVisit("Evacuacion");
            try {
                state.getDb().appendExpense(new ExpenseRecord(0L, getName(), 500, getDescription(), LocalDateTime.now()));
            } catch (SQLException e) {
                e.printStackTrace();
            }
        });
        state.updateTotalExpenses(500.0);
    }

    @Override
    public EventRecord toRecord(long step) {
        return new EventRecord(0L, step, getName(), getDescription(), "Tourist", LocalDateTime.now());
    }

    @Override
    public double getProbability() {
        return probability;
    }

}
