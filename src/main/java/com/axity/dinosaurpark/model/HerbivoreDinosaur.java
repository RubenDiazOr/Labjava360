package com.axity.dinosaurpark.model;

public class HerbivoreDinosaur extends Dinosaur {

    public HerbivoreDinosaur(int id, String name, String species, DinosaurStatus status, double feedingCostPerDay) {
        super(id, name, species, status, feedingCostPerDay);
    }

    @Override
    public String getDiet() {
        return "HERBIVORE";
    }

    @Override
    public double getDangerLevel() {
       return 0.2;
    }

    
}
