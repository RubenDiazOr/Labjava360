package com.axity.dinosaurpark.zone;

import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.Random;

import com.axity.dinosaurpark.model.Tourist;
import com.axity.dinosaurpark.persistence.DatabaseService;
import com.axity.dinosaurpark.persistence.ExpenseRecord;
import com.axity.dinosaurpark.simulation.ParkState;

public class PowerPlant implements ParkZone {

    private final ParkState state;
    private boolean statusOperation;
    private int currentOccupancy;
    private double initialEnerg;

    public PowerPlant(ParkState state) {
        this.state = state;
        this.statusOperation = true;
        this.initialEnerg = state.getConfig().getDouble("powerplant.initialEnergy", 100.0);
    }

    @Override
    public String getName() {
        return "POWER PLANT ZONE";
    }

    @Override
    public boolean hasCapacity() {
        return (getCurrentOccupancy() < getMaxCapacity()) ? true : false;
    }

    @Override
    public int getCurrentOccupancy() {
        return currentOccupancy;
    }

    @Override
    public int getMaxCapacity() {
        return state.getConfig().getInt("powerplant.maxCapacity", 20);
    }

    @Override
    public void enter(Tourist tourist) {
        if (hasCapacity()) {
            currentOccupancy++;
        }
    }

    @Override
    public void exit(Tourist tourist) {
        currentOccupancy--;
    }

    public void tick(Random failureProbability, DatabaseService dService) throws SQLException{
        initialEnerg -= state.getConfig().getDouble("powerplant.consumptionPerStep", 1.5);
        if (failureProbability.nextDouble() < state.getConfig().getDouble("powerplant.failureProbability", 0.05)) {
            triggerFailure(dService);
        }
    }

    public double getRemainigEnergy(){
        return initialEnerg;
    }

    public boolean isOperational() {
        return statusOperation;
    }

    public void triggerFailure(DatabaseService dService) throws SQLException{
        statusOperation = false;
        dService.appendExpense(new ExpenseRecord(0L, "Falla Electrica", 2000.00, "Fallo masivo en Planta Electrica", LocalDateTime.now()));
    }

    public void repair(){
        statusOperation = true;
    }

}
