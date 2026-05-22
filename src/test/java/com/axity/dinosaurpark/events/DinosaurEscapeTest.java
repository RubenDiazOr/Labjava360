package com.axity.dinosaurpark.events;

import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.junit.Test;

import com.axity.dinosaurpark.config.ParkConfig;
import com.axity.dinosaurpark.model.CarnivoreDinosaur;
import com.axity.dinosaurpark.model.Dinosaur;
import com.axity.dinosaurpark.model.DinosaurStatus;
import com.axity.dinosaurpark.model.HerbivoreDinosaur;
import com.axity.dinosaurpark.model.Tourist;
import com.axity.dinosaurpark.model.TouristStatus;
import com.axity.dinosaurpark.simulation.ParkState;

public class DinosaurEscapeTest {
    private ParkConfig parkConfig;
    private DinosaurEscapeEvent dinoEscape;
    protected StormEvent storm;
    private ParkState state;
    

    public DinosaurEscapeTest() throws IOException{
        parkConfig = ParkConfig.getInstance();
        dinoEscape = new DinosaurEscapeEvent(0.3);
        this.storm = new StormEvent(0.5);
        state = new ParkState(parkConfig);
        state.setTourists(initializeTourist(state));
        state.setDinosaurs(initializedDinosaurs(state));
    }

    @Test
    public void dinosarEscape(){
        dinoEscape.execute(state, new Random());
        assertTrue(
            state.getDinosaurs().stream().filter(d -> d.getStatus().equals(DinosaurStatus.ESCAPED)).findFirst().isPresent() 
        );
    }

    public void stormEvent(){
        storm.execute(state, new Random());
        assertTrue(state.getTourists().stream().filter(t -> t.getStatus().equals(TouristStatus.EXITED)).findFirst().isPresent());

        assertTrue(state.getTotalExpenses() > 0);
    }

    
    private List<Dinosaur> initializedDinosaurs(ParkState state) {
        List<Dinosaur> dinosaurs = new ArrayList<>();
        
        for (int i = 0; i < state.getConfig().getInt("dinosaurs.carnivores", 5); i++) {
            dinosaurs.add(
                    new CarnivoreDinosaur(i + 20, "Carnivor" + i, "Tiranosaurio", DinosaurStatus.IN_ENCLOSURE, 200));
        }
        for (int i = 0; i < state.getConfig().getInt("dinosaurs.herbivores", 5); i++) {
            dinosaurs.add(
                    new HerbivoreDinosaur(i + 30, "Herbivore" + i, "Cuello largo", DinosaurStatus.IN_ENCLOSURE, 100));
        }
        return dinosaurs;
    }

    private List<Tourist> initializeTourist(ParkState state){
        List<Tourist> tourists = new ArrayList<>();
        for (int i = 0; i < state.getConfig().getDouble("tourists", 20); i++) {
            Tourist t = new Tourist();
            t.setStatus(TouristStatus.IN_PARK);
            tourists.add(t);
        }
        return tourists;
    }

}
