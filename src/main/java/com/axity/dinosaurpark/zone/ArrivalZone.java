package com.axity.dinosaurpark.zone;


import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import com.axity.dinosaurpark.model.Tourist;
import com.axity.dinosaurpark.model.TouristStatus;
import com.axity.dinosaurpark.persistence.DatabaseService;
import com.axity.dinosaurpark.persistence.RevenueRecord;
import com.axity.dinosaurpark.simulation.ParkState;

public class ArrivalZone implements ParkZone{

    private final ParkState state;
    private int currentOccupancy;

    public ArrivalZone(ParkState state) {
        this.state = state;
    }

    @Override
    public String getName() {
        return "ARRIVAL ZONE";
    }

    @Override
    public boolean hasCapacity() {
        return (getCurrentOccupancy() < getCurrentOccupancy()) ? true : false;
    }

    @Override
    public int getCurrentOccupancy() {
       return currentOccupancy;
    }

    @Override
    public int getMaxCapacity() {
        return state.getConfig().getInt("arrival.maxCapacity", 30);
    }

    @Override
    public void enter(Tourist tourist) {
        if (hasCapacity()) {
            currentOccupancy += 1;
            tourist.setStatus(TouristStatus.IN_PARK);
            tourist.spend(state.getConfig().getDouble("arrival.ticketPrice", 25.0));
        }
    }

    @Override
    public void exit(Tourist tourist) {
        currentOccupancy -= 1;
        tourist.setStatus(TouristStatus.EXITED);
    }

    public List<Tourist> processBatch(int batchSize, DatabaseService db, double discount){
        List<Tourist> tourists = new ArrayList<>();
        for (int i=0; i<batchSize; i++) {
            Tourist tourist = new Tourist();
            tourist.setStatus(TouristStatus.IN_PARK);
            double price = state.getConfig().getDouble("arrival.ticketPrice", 25.0);
            double priceWithDiscount = price - ( price * discount) ;
            tourist.spend(priceWithDiscount);
            try {
                db.appendRevenue(new RevenueRecord(0L, "Entrada", priceWithDiscount, tourist.getId(), getName(), LocalDateTime.now()));
            } catch (SQLException e) {
                e.printStackTrace();
            }
            tourists.add(tourist);
        }
        return tourists;
    }

}
