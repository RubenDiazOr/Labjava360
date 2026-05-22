package com.axity.dinosaurpark.persistence;

import java.time.LocalDateTime;

public record RevenueRecord(long id, String type, double amount, long touristId, String zone, LocalDateTime timestamp) {
   
}
