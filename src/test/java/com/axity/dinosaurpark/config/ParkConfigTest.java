package com.axity.dinosaurpark.config;

import static org.junit.Assert.assertEquals;

import java.io.IOException;

import org.junit.Test;

public class ParkConfigTest {
    
    @Test
    public void singlentonParkFuncntional() throws IOException{
        ParkConfig config = ParkConfig.getInstance();
        assertEquals(30, config.getInt("tourists", 30));
        assertEquals("./data/parkdb", config.getString("db.path", "./data/parkdb"));
    }
}
