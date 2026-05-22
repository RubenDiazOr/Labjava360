package com.axity.dinosaurpark.simulation;

import static org.junit.Assert.assertFalse;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.IOException;

import org.junit.Test;

import com.axity.dinosaurpark.config.ParkConfig;
import com.axity.dinosaurpark.model.TouristStatus;

public class SimulationEngineTest {
    
    @Test
    public void engineSimulation() throws IOException, Exception{
        ParkState state = new ParkState(ParkConfig.getInstance());
        SimulationEngine simulationEngine = new SimulationEngine(state);
        simulationEngine.initializeVehicules();
        assertEquals(
            state.getVehicules().size(), 
            state.getConfig().getInt("vehicules", 5)
        );
    }

    @Test
    public void noOneInPark() throws Exception{
        ParkState state = new ParkState(ParkConfig.getInstance());
        SimulationEngine simulationEngine = new SimulationEngine(state);
        simulationEngine.run();
        assertFalse(state.getTourists().stream().filter(t -> t.getStatus().equals(TouristStatus.IN_PARK)).toList().size() > 0);
    }
}
