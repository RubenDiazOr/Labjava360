package com.axity.dinosaurpark.monitoring;

import org.junit.Test;

import com.axity.dinosaurpark.config.ParkConfig;
import com.axity.dinosaurpark.simulation.ParkState;
import com.axity.dinosaurpark.simulation.SimulationEngine;

public class AppMonitoringTest {
    
    @Test
    public void monitoringApp() throws Exception{
        ParkState state = new ParkState(ParkConfig.getInstance());
        SimulationEngine simulation = new SimulationEngine(state);
        simulation.initializeZones();
        ParkMonitor.displaySnapshot(state);
    }
}
