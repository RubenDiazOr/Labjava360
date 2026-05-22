package com.axity.dinosaurpark.zones;

import static org.junit.Assert.assertEquals;

import java.io.IOException;

import org.junit.Test;

import com.axity.dinosaurpark.config.ParkConfig;
import com.axity.dinosaurpark.model.Tourist;
import com.axity.dinosaurpark.model.TouristStatus;
import com.axity.dinosaurpark.simulation.ParkState;
import com.axity.dinosaurpark.zone.ArrivalZone;

public class ArribalZoneTest {
    
    @Test
    public void createArrivalZone() throws IOException{
        ArrivalZone arrivalZone = new ArrivalZone(new ParkState(ParkConfig.getInstance()));
        Tourist tourist = new Tourist();
        arrivalZone.enter(tourist);
        assertEquals(TouristStatus.WAITING, tourist.getStatus());
    }

    @Test
    public void getNombreZone() throws IOException{
        ArrivalZone arrivalZone = new ArrivalZone(new ParkState(ParkConfig.getInstance()));
        assertEquals("ARRIVAL ZONE", arrivalZone.getName()); 
    }
}
