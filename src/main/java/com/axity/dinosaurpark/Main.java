package com.axity.dinosaurpark;

import com.axity.dinosaurpark.config.ParkConfig;
import com.axity.dinosaurpark.simulation.ParkState;
import com.axity.dinosaurpark.simulation.SimulationEngine;

/**
 * Hello world!
 *
 */
public class Main 
{
    public static void main( String[] args ) throws Exception{
        ParkConfig config = ParkConfig.getInstance();
        ParkState state = new ParkState(config);
        new SimulationEngine(state).run();
    }
}
