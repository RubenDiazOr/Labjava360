package com.axity.dinosaurpark.zone;

import java.util.Random;

import com.axity.dinosaurpark.model.Tourist;
import com.axity.dinosaurpark.persistence.DatabaseService;
import com.axity.dinosaurpark.simulation.ParkState;

public class CentralHub implements ParkZone{

    private final ParkState state;
    private int currentOccupancy;

    
    public CentralHub(ParkState state) {
        this.state = state;
    }

    @Override
    public String getName() {
        return "CENTRAL HUB";
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
        return state.getConfig().getInt("central.maxCapacity", 30);
    }

    @Override
    public void enter(Tourist tourist) {
        if (hasCapacity()) {
            currentOccupancy += 1;
        }
    }

    @Override
    public void exit(Tourist tourist) {
        currentOccupancy -= 1;
    }

    public void visit(Tourist tourist, Random souvenirProbability, DatabaseService databaseService, double discount){
        enter(tourist);
        if (souvenirProbability.nextDouble() < state.getConfig().getDouble("hub.souvenirPurchaseProbability", 0.4)) {
            double price = state.getConfig().getDouble("hub.souvenirPrice", 15.0);
            double priceWithDiscount = price - (price * discount);
            state.getTourists().get(state.getTourists().indexOf(tourist)).spend(priceWithDiscount);
            System.out.println("SOUVENIR VENDIDO");
        }
    }

}
