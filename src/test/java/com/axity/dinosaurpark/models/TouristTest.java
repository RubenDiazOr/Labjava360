package com.axity.dinosaurpark.models;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import com.axity.dinosaurpark.model.Tourist;
import com.axity.dinosaurpark.model.TouristStatus;

public class TouristTest {
    
    @Test
    public void createTourist(){
        Tourist tourist = new Tourist();
        assertEquals(TouristStatus.WAITING, tourist.getStatus());
    }

    @Test
    public void spendOk(){
        Tourist tourist = new Tourist();
        tourist.spend(20.0);
        assertTrue(tourist.getMoneySpend() > 0);
    }
}
