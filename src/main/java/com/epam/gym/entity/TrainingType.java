package com.epam.gym.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
public class TrainingType {
    private String id;
    private String trainingType;
    public TrainingType( String trainingType) {
        this.trainingType = trainingType;
        id = UUID.randomUUID().toString();
    }
}
