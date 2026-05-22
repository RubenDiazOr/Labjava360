package com.axity.dinosaurpark.events;

import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.util.Random;

import org.junit.Test;

import com.axity.dinosaurpark.config.ParkConfig;
import com.axity.dinosaurpark.simulation.ParkState;

public class DealHourEventTest {
    
    @Test
    public void eventExecute() throws IOException{
        ParkState state = new ParkState(ParkConfig.getInstance());
        DealHourEvent dealHourEvent = new DealHourEvent(1);
        if (0.3 < dealHourEvent.getProbability()) {     
            dealHourEvent.execute(state, new Random());
            assertTrue(state.isDealHourActive());
        }
    }

    @Test
    public void currentDiscountOK() throws IOException{
        ParkState state = new ParkState(ParkConfig.getInstance());
        DealHourEvent dealHourEvent = new DealHourEvent(1);
        if (0.3 < dealHourEvent.getProbability()) {     
            dealHourEvent.execute(state, new Random());
            assertTrue(state.getCurrentDiscount() == 0.3);
        }
    }

    @Test
    public void currentDiscountCero() throws IOException{
        ParkState state = new ParkState(ParkConfig.getInstance());
        DealHourEvent dealHourEvent = new DealHourEvent(1);
        if (10 < dealHourEvent.getProbability()) {     
            dealHourEvent.execute(state, new Random());
            state.clearActiveEvents();
            assertTrue(state.getCurrentDiscount() == 0);
        }
    }
}
