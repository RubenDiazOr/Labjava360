package com.axity.dinosaurpark.model;

import java.util.List;
import java.util.Optional;

import com.axity.dinosaurpark.zone.PowerPlant;

public class Technician extends Worker{

    
    public Technician(int id, String name, double dailySalary) {
        super(id, name, dailySalary);
    }

    @Override
    public String getRole() {
        return "TECHNICIAN";
    }

    public void repairIfNeeded(PowerPlant plant, List<Vehicule> vehicules){
        if (!plant.isOperational()) {
            Optional<Vehicule> avaliableVehicule = vehicules.stream()
                .filter(veh -> veh.getStatus().equals(VehiculeStatus.AVAILABLE))
                .findFirst();

            avaliableVehicule.ifPresent(v -> {
                v.use();
                plant.repair();
                v.free();
            });
        }
    }

}
