package com.axity.dinosaurpark.model;

import java.time.LocalDateTime;

public class Ticket {
    private final int id;
    private final int touristId;
    private final double price;
    private final String category;
    private final LocalDateTime issuedAt;

    public Ticket(int id, int touristId, double price, String category, LocalDateTime issuedAt) {
        this.id = id;
        this.touristId = touristId;
        this.price = price;
        this.category = category;
        this.issuedAt = issuedAt;
    }

    public int getId() {
        return id;
    }

    public int getTouristId() {
        return touristId;
    }

    public double getPrice() {
        return price;
    }

    public String getCategory() {
        return category;
    }

    public LocalDateTime getIssuedAt() {
        return issuedAt;
    }

}
