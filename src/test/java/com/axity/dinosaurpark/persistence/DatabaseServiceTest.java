package com.axity.dinosaurpark.persistence;

import java.io.IOException;
import java.time.LocalDateTime;

import org.junit.Test;

import com.axity.dinosaurpark.config.ParkConfig;

public class DatabaseServiceTest {
    
    @Test
    public void saveInfo() throws IOException, Exception{
        DatabaseService db = new DatabaseService(ParkConfig.getInstance().getString("db.path", "./data/parkdb"));
        db.appendRevenue(new RevenueRecord(0L, "Prueba", 200.0 ,1L, "ZONE", LocalDateTime.now()));
    }
}
