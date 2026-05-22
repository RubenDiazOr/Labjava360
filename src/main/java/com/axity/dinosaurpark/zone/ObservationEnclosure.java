package com.axity.dinosaurpark.zone;

import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.Random;

import com.axity.dinosaurpark.model.Tourist;
import com.axity.dinosaurpark.persistence.DatabaseService;
import com.axity.dinosaurpark.persistence.RevenueRecord;
import com.axity.dinosaurpark.simulation.ParkState;

public class ObservationEnclosure implements ParkZone {

    private final ParkState state;
    private int currentOccupancy;

    public ObservationEnclosure(ParkState state) {
        this.state = state;
    }

    @Override
    public String getName() {
        return "OBSERVATION_ENCLOSURE_ZONE";
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
        return state.getConfig().getInt("enclosure.maxVisitors", 37);
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

    public void visit(Tourist tourist, Random probability, DatabaseService dService) {
        double value = probability.nextDouble();
        if (value > 0.06) {
            double basic = state.getConfig().getDouble("enclosure.basic.entryFee", 20.0);
            state.getTourists().get(state.getTourists().indexOf(tourist)).spend(basic);
            System.out.println("ENCLOUSERE_BASIC_VENDIDO");
            try {
                dService.appendRevenue(
                        new RevenueRecord(0L, "ENCLOUSERE_BASIC", basic,
                                0L, getName(), LocalDateTime.now()));
            } catch (SQLException e) {
                e.printStackTrace();
            }
        } else {
            if (value < 0.03) {
                double vip = state.getConfig().getDouble("enclosure.vip.entryFee", 30.0);
                state.getTourists().get(state.getTourists().indexOf(tourist)).spend(vip);
                System.out.println("ENCLOUSERE_VIP_VENDIDO");
                try {
                    dService.appendRevenue(
                            new RevenueRecord(0L, "ENCLOUSERE_BASIC", vip,
                                    0L, getName(), LocalDateTime.now()));
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            } else {
                if (value > 0.03) {
                    double premium = state.getConfig().getDouble("enclosure.premium.entryFee", 30.0);
                    state.getTourists().get(state.getTourists().indexOf(tourist)).spend(premium);
                    System.out.println("ENCLOUSERE_PREMIUM_VENDIDO");
                    try {
                        dService.appendRevenue(
                                new RevenueRecord(0L, "ENCLOUSERE_BASIC", premium,
                                        0L, getName(), LocalDateTime.now()));
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                }
            }
        }

    }

    public void conductSurvey(Tourist tourist, Random num) {

    }

}
