package com.axity.dinosaurpark.monitoring;

import com.axity.dinosaurpark.model.DinosaurStatus;
import com.axity.dinosaurpark.model.TouristStatus;
import com.axity.dinosaurpark.simulation.ParkState;
import com.axity.dinosaurpark.zone.PowerPlant;

public class ParkMonitor {

    public static void displaySnapshot(ParkState state) {
        System.out.println("____________________________________");
        System.out.printf("| MONITOR - Step %-17d |%n", state.getCurrentStep());
        System.out.printf("| 1. Turistas IN_PARK:   %-10d|%n", state.getTourists().stream().filter(t -> t.getStatus().equals(TouristStatus.IN_PARK)).toList().size());
        int dinosaursInEnclosure = state.getDinosaurs().stream()
                .filter(d -> d.getStatus().equals(DinosaurStatus.IN_ENCLOSURE)).toList().size();
        System.out.printf("| 2. Dinosaurios en encierro:  %-4d|%n", dinosaursInEnclosure);
        PowerPlant powerPlant = (PowerPlant) state.getZones().get("PowerPlant");
        System.out.printf("| 3. Energia disponible:   %-8s|%n",powerPlant.getRemainigEnergy());
        System.out.printf("| 4. Eventos activos:      %-8d|%n", state.getActiveEventNames().size());
        System.out.printf("| 5. Vehiculos no disponibles: %-4d|%n", state.countVehiculesInUse());
        System.out.println("|__________________________________|");
    }
}
