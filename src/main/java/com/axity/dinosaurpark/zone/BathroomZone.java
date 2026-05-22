package com.axity.dinosaurpark.zone;

import java.util.Random;

import com.axity.dinosaurpark.model.Tourist;
import com.axity.dinosaurpark.persistence.DatabaseService;
import com.axity.dinosaurpark.simulation.ParkState;

public class BathroomZone implements ParkZone {

    private final ParkState state;
    private int currentOccupancy;
    private int useDurationSteps;

    public BathroomZone(ParkState state) {
        this.state = state;
    }

    @Override
    public String getName() {
        return "BATHROOM ZONE";
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
        return state.getConfig().getInt("bathroom.maxCapacity", 10);
    }

    @Override
    public void enter(Tourist tourist) {
        if (hasCapacity()) {
            currentOccupancy++;
            useDurationSteps = state.getConfig().getInt("bathroom.useDurationSteps", 3);
        }
    }

    @Override
    public void exit(Tourist tourist) {
        currentOccupancy--;
    }

    public void tryEnter(Tourist tourist, Random SPAProbability, DatabaseService dService){
        enter(tourist);
        if(SPAProbability.nextDouble() < state.getConfig().getDouble("bathroom.spaPurchaseProbability", 0.2)){
            state.getTourists().get(state.getTourists().indexOf(tourist)).spend(state.getConfig().getDouble("bathroom.spaPrice", 20));            
            System.out.println("SPA VENDIDO");
        }
    }

    public void tick(){
        if (useDurationSteps > 0) {
            useDurationSteps--;
        } else {
            currentOccupancy--;
        }
    }

}
