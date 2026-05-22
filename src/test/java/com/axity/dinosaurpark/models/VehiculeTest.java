package com.axity.dinosaurpark.models;

import static org.junit.Assert.assertTrue;

import java.io.IOException;

import org.junit.Test;

import com.axity.dinosaurpark.config.ParkConfig;
import com.axity.dinosaurpark.model.Vehicule;
import com.axity.dinosaurpark.model.VehiculeStatus;

public class VehiculeTest {
    
    @Test
    public void initialState() throws IOException{
        Vehicule vehicule = new Vehicule(ParkConfig.getInstance());
        assertTrue(vehicule.getStatus().equals(VehiculeStatus.AVAILABLE));
    }

    @Test
    public void useMethod() throws IOException{
        Vehicule vehicule = new Vehicule(ParkConfig.getInstance());
        vehicule.use();
        assertTrue(vehicule.getStatus().equals(VehiculeStatus.IN_USE));
    }

    @Test
    public void freeMethod() throws IOException{
        Vehicule vehicule = new Vehicule(ParkConfig.getInstance());
        vehicule.use();
        vehicule.free();
        assertTrue(vehicule.getStatus().equals(VehiculeStatus.AVAILABLE));
    }

    @Test
    public void markBrokenMethod() throws IOException{
        Vehicule vehicule = new Vehicule(ParkConfig.getInstance());
        vehicule.markBroken();
        assertTrue(vehicule.getStatus().equals(VehiculeStatus.BROKEN));
    }
}
