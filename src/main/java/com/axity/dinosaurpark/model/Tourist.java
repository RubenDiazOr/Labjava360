package com.axity.dinosaurpark.model;

import java.util.ArrayList;
import java.util.List;

public class Tourist {
    private long id;
    private String name;
    private TouristStatus status;
    private double moneySpend;
    private List<String> visitedZones;
    
    public Tourist(){
        visitedZones = new ArrayList<>();
        status = TouristStatus.WAITING;
        this.moneySpend = 0.0;
    }
    
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public TouristStatus getStatus() {
        return status;
    }

    public void setStatus(TouristStatus status) {
        this.status = status;
    }

    public double getMoneySpend() {
        return moneySpend;
    }

    public List<String> getVisitedZones() {
        return visitedZones;
    }

    public void setVisitedZones(List<String> visitedZones) {
        this.visitedZones = visitedZones;
    }

    public void spend(double moneySpent){
        this.moneySpend += moneySpent;
    }

    public void recordVisit(String type){
        setStatus(TouristStatus.EXITED);
    }
}
