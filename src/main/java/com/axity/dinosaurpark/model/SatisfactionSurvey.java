package com.axity.dinosaurpark.model;

public class SatisfactionSurvey {
    private final int touristId;
    private final String enclouseName;
    private final int score;
    
    public SatisfactionSurvey(int touristId, String enclouseName, int score) {
        this.touristId = touristId;
        this.enclouseName = enclouseName;
        if (score > 5) {
            this.score = 5;
        } else {
            this.score = score;
        }
    }

    public int getTouristId() {
        return touristId;
    }

    public String getEnclouseName() {
        return enclouseName;
    }

    public int getScore() {
        return score;
    }

    
}
