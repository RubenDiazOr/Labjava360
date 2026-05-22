package com.axity.dinosaurpark.events;

import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.util.List;
import java.util.Random;

import org.junit.Test;

import com.axity.dinosaurpark.config.ParkConfig;
import com.axity.dinosaurpark.model.Vehicule;
import com.axity.dinosaurpark.model.VehiculeStatus;
import com.axity.dinosaurpark.simulation.ParkState;

public class VehiculeFailureEventTest {
    
    @Test
    public void vehiculeAvaliable() throws IOException{
        ParkState state = new ParkState(ParkConfig.getInstance());
        state.setVehicules(List.of(new Vehicule(state.getConfig())));
        VehiculeFailureEvent vehiculeFailureEvent = new VehiculeFailureEvent(1);
        if (10 < vehiculeFailureEvent.getProbability()) {
            vehiculeFailureEvent.execute(state, new Random());
            assertTrue(state.getVehicules().get(0).getStatus().equals(VehiculeStatus.BROKEN));
        }
    }

    @Test
    public void vehiculosInUse() throws IOException{
        ParkState state = new ParkState(ParkConfig.getInstance());
        state.setVehicules(List.of(new Vehicule(state.getConfig())));
        state.getVehicules().get(0).use();
        VehiculeFailureEvent vehiculeFailureEvent = new VehiculeFailureEvent(1);
        if (10 < vehiculeFailureEvent.getProbability()) {
            vehiculeFailureEvent.execute(state, new Random());
        }
    }

}
