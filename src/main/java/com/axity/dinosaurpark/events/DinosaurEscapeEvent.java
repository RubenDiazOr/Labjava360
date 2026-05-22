package com.axity.dinosaurpark.events;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

import com.axity.dinosaurpark.model.Dinosaur;
import com.axity.dinosaurpark.model.DinosaurStatus;
import com.axity.dinosaurpark.model.Tourist;
import com.axity.dinosaurpark.model.TouristStatus;
import com.axity.dinosaurpark.persistence.EventRecord;
import com.axity.dinosaurpark.simulation.ParkState;

public class DinosaurEscapeEvent implements SimulationEvent{

    private final double probability;

    public DinosaurEscapeEvent(double probability){
        this.probability = probability;
    }
    
    @Override
    public String getName() {
        return "ESCAPE_DINOSAURIO";
    }

    @Override
    public String getDescription() {
        return "Escape de dinosaurio en areas de encierro";
    }

    @Override
    public void execute(ParkState state, Random rng) {
        System.out.println("---------------------------------------");
        System.out.println("EVENTO EN EJECUCION "+getName());
        System.out.println("---------------------------------------");
        state.setEventToAllEventsPerDays(getName());
        List<Dinosaur> dinoInclousere = state.getDinosaurs().stream().filter(d -> d.getStatus().equals(DinosaurStatus.IN_ENCLOSURE)).collect(Collectors.toList());
        if(dinoInclousere.size() > 0){
            int dinoToEscape = rng.nextInt(dinoInclousere.size());
            state.getDinosaurs().get(dinoToEscape).escape();
            if (rng.nextDouble() < state.getDinosaurs().get(dinoToEscape).getDangerLevel()) {
                List<Tourist> touristsInPark = state.getTourists().stream().filter(t -> t.getStatus().equals(TouristStatus.IN_PARK)).collect(Collectors.toList());
                if (touristsInPark.size() > 0) {
                    state.getTourists().get(rng.nextInt(touristsInPark.size())).setStatus(TouristStatus.ATTACKED);
                } else {
                    System.out.println("SIN TURISTISTAS EN PARQUE");
                }
            }
        } else {
            System.out.println("DINOSAURIOS SUELTOS");
        }
    }

    @Override
    public EventRecord toRecord(long step) {
        return new EventRecord(0L, step, getName(), getDescription(), "Dinosaur, Tourist, Guard", LocalDateTime.now());
    }

    @Override
    public double getProbability() {
        return probability;
    }

}
