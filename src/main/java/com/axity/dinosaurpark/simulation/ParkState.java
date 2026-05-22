package com.axity.dinosaurpark.simulation;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import com.axity.dinosaurpark.config.ParkConfig;
import com.axity.dinosaurpark.model.Dinosaur;
import com.axity.dinosaurpark.model.DinosaurStatus;
import com.axity.dinosaurpark.model.Tourist;
import com.axity.dinosaurpark.model.TouristStatus;
import com.axity.dinosaurpark.model.Vehicule;
import com.axity.dinosaurpark.model.VehiculeStatus;
import com.axity.dinosaurpark.model.Worker;
import com.axity.dinosaurpark.persistence.DatabaseService;
import com.axity.dinosaurpark.zone.ParkZone;

public class ParkState {
    private int currentStep;
    private List<Tourist> tourists;
    private List<Dinosaur> dinosaurs;
    private List<Worker> workers;
    private Map<String, ParkZone> zones;
    // private CsvWritter csvWritter;
    private double totalRevenue;
    private double totalExpenses;
    private double totalSale;

    private List<Vehicule> vehicules;
    private List<String> activeEventNames;
    private List<String> allEventsPerDay;
    private boolean dealHourActive;
    private double currentDiscount;
    private DatabaseService db;
    private final Random rng;
    private final ParkConfig config;

    public ParkState(ParkConfig pConfig) throws IOException {
        this.currentStep = 0;
        this.totalExpenses = 0;
        this.totalRevenue = 0;
        this.currentDiscount = 0;
        this.totalSale = 0;
        this.config = pConfig;
        this.tourists = new ArrayList<>();
        this.dinosaurs = new ArrayList<>();
        this.workers = new ArrayList<>();
        this.zones = new HashMap<>();
        this.vehicules = new ArrayList<>();
        this.activeEventNames = new ArrayList<>();
        this.rng = new Random();
        this.allEventsPerDay = new ArrayList<>();
        // this.db = DatabaseService
    }

    public boolean isDealHourActive() {
        return dealHourActive;
    }

    public double getCurrentDiscount() {
        return currentDiscount;
    }

    public void setDealHourActive(boolean dealHourActive) {
        this.dealHourActive = dealHourActive;
    }

    public void setCurrentDiscount(double currentDiscount) {
        this.currentDiscount = currentDiscount;
    }

    public ParkConfig getConfig() {
        return config;
    }

    public List<String> getAllEventsPerDay() {
        return allEventsPerDay;
    }

    public void setEventToAllEventsPerDays(String eventName) {
        allEventsPerDay.add(eventName);
    }

    public DatabaseService getDb() {
        return db;
    }

    public void setDb(DatabaseService db) {
        this.db = db;
    }

    public Random getRng() {
        return rng;
    }

    public void incrementStep() {
        this.currentStep++;
    }

    public int getCurrentStep() {
        return currentStep;
    }

    public double getTotalExpenses() {
        workers.forEach(w -> {
            totalExpenses += w.getDailySalary();
        });

        dinosaurs.forEach(dino -> {
            totalExpenses += dino.getFeedingCostPerDay();
        });

        return totalExpenses;
    }

    public void updateTotalExpenses(double t) {
        this.totalExpenses = this.totalExpenses + t;
    }

    public void updateTotalRevenue(double rv) {
        this.totalRevenue = this.totalRevenue + rv;
    }

    public double getSales(){  
        tourists.forEach(t -> {
            totalSale += t.getMoneySpend();
        });
        return totalSale;
    }

    public double getTotalRevenue() {
        tourists.forEach(t -> {
            totalRevenue += t.getMoneySpend();
        });
        return totalRevenue-totalExpenses;
    }

    public List<Tourist> getTourists() {
        return tourists;
    }

    public void setTourists(List<Tourist> tourists) {
        for (Tourist t : tourists) {
            this.tourists.add(t);
        }
    }

    public void setTourist(Tourist t) {
        this.tourists.add(t);
    }

    public List<Dinosaur> getDinosaurs() {
        return dinosaurs;
    }

    public void setDinosaurs(List<Dinosaur> dinosaurs) {
        for (Dinosaur d : dinosaurs) {
            this.dinosaurs.add(d);
        }
    }

    public void setDionosaur(Dinosaur d) {
        dinosaurs.add(d);
    }

    public List<Worker> getWorkers() {
        return workers;
    }

    public void setWorkers(List<Worker> workers) {
        this.workers = workers;
    }

    public Map<String, ParkZone> getZones() {
        return zones;
    }

    public void setZones(Map<String, ParkZone> zones) {
        this.zones = zones;
    }

    public List<Vehicule> getVehicules() {
        return vehicules;
    }

    public void setVehicules(List<Vehicule> vehicules) {
        this.vehicules = vehicules;
    }

    public List<String> getActiveEventNames() {
        return activeEventNames;
    }

    public void setActiveEventNames(List<String> activeEventNames) {
        this.activeEventNames = activeEventNames;
    }

    public int countActiveTourist() {
        return tourists.stream().filter(tourist -> tourist.getStatus().equals(TouristStatus.IN_PARK)).toList().size();
    }

    public int countDinosaursInEnclosure() {
        return dinosaurs.stream().filter(dino -> dino.getStatus().equals(DinosaurStatus.IN_ENCLOSURE)).toList().size();
    }

    public void addActiveEvent(String event) {
        activeEventNames.add(event);
    }

    public void clearActiveEvents() {
        currentDiscount = 0;
        activeEventNames.clear();
        dealHourActive = false;
    }

    public int countVehiculesInUse() {
        return vehicules.stream().filter(v -> v.getStatus().equals(VehiculeStatus.IN_USE)).toList().size();
    }

}
