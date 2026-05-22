package com.axity.dinosaurpark.model;


public abstract class Dinosaur {
    private final int id;
    private final String name;
    private final String species;
    private DinosaurStatus status;
    private final double feedingCostPerDay;

    protected Dinosaur(int id, String name, String species, DinosaurStatus status,double feedingCostPerDay){
        this.id = id;
        this.name = name;
        this.species = species;
        this.status = status;
        this. feedingCostPerDay = feedingCostPerDay;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getSpecies() {
        return species;
    }

    public double getFeedingCostPerDay() {
        return feedingCostPerDay;
    }

    public abstract String getDiet();
    public abstract double getDangerLevel();
     
    public void escape() {
        status = DinosaurStatus.ESCAPED;
    }

    public void recapture(){
        status = DinosaurStatus.RECAPTURED;
    }

    public void returnToEnclosure(){
        status = DinosaurStatus.IN_ENCLOSURE;
    }

    public DinosaurStatus getStatus(){
        return status;
    }
}
