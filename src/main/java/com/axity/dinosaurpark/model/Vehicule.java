package com.axity.dinosaurpark.model;

import com.axity.dinosaurpark.config.ParkConfig;

public class Vehicule {

    private VehiculeStatus status;
    private final int repairSteps;
    private int repairCountdown;

    public Vehicule(ParkConfig pConfig) {
        this.repairSteps = pConfig.getInt("vehicles.repairSteps", 5);
        this.repairCountdown = 0;
        this.status = VehiculeStatus.AVAILABLE;
    }

    public VehiculeStatus getStatus() {
        return status;
    }

    public void use() {
        status = VehiculeStatus.IN_USE;
    }

    public void free() {
        status = VehiculeStatus.AVAILABLE;
    }

    public void markBroken() {
        status = VehiculeStatus.BROKEN;
        repairCountdown = repairSteps;
    }

    public void tick() {
        if (status.equals(VehiculeStatus.BROKEN)) {
            repairCountdown--;
            if (repairCountdown <= 0) {
                status = VehiculeStatus.AVAILABLE;
            }
        }
    }
}
