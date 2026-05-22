package com.axity.dinosaurpark.zones;

import static org.junit.Assert.assertTrue;

import java.io.IOException;

import org.junit.Test;

import com.axity.dinosaurpark.config.ParkConfig;
import com.axity.dinosaurpark.model.Tourist;
import com.axity.dinosaurpark.simulation.ParkState;
import com.axity.dinosaurpark.zone.BathroomZone;

public class BathroomZoneTest {
    
    @Test
    public void touristEnterInBathroom() throws IOException{
        BathroomZone bathroomZone = new BathroomZone(new ParkState(ParkConfig.getInstance()));
        Tourist tourist = new Tourist();
        bathroomZone.enter(tourist);
        assertTrue(bathroomZone.getCurrentOccupancy() > 0);
    }

    @Test
    public void getNombreZone() throws IOException{
        BathroomZone bathroomZone = new BathroomZone(new ParkState(ParkConfig.getInstance()));
        assertTrue(bathroomZone.getName().length() > 0); 
    }    
}
