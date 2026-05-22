package com.axity.dinosaurpark.config;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public final class ParkConfig {

    private static ParkConfig instance;
    private final Properties props;

    private ParkConfig() throws IOException {
        props = new Properties();
        try (InputStream is = getClass().getClassLoader()
                .getResourceAsStream("park.properties")) {
            if (is == null)
                throw new RuntimeException("Archivo no encontrado");
            props.load(is);
        }
    }

    public static ParkConfig getInstance() throws IOException {
        if (instance == null) {
            return new ParkConfig();
        }
        return instance;
    }

    public int getInt(String key, int defaultValue){ 
        return Integer.parseInt(props.getProperty(key, String.valueOf(defaultValue)));
    }

    public double getDouble(String key, double defaultValue){
        return Double.parseDouble(props.getProperty(key, String.valueOf(defaultValue)));
    }

    public String getString(String key, String defaultValue){
        return props.getProperty(key, defaultValue);
    }

    public long getSeed(){
        return Long.parseLong(props.getProperty("simulation.seed"));
    } // lee simulation.seed

    public int getTotalSteps(){
        return Integer.parseInt(props.getProperty("simulation.totalSteps"));
    }


    // Solo para tests — permite resetear la instancia entre tests
    static void resetForTesting() {
        instance = null;
    }
}
