package com.axity.dinosaurpark.model;

import java.util.List;

public class Guard extends Worker {

    public Guard(int id, String name, double dailySalary) {
        super(id, name, dailySalary);
    }

    @Override
    public String getRole() {
        return "GUARD";
    }

    public void recaptureEscapedDinosaurs(List<Dinosaur> dinosaurs){
        dinosaurs.forEach(dino -> {
            if (dino.getStatus().equals(DinosaurStatus.ESCAPED)) {
                dino.returnToEnclosure();
            }
        });
    }

}
