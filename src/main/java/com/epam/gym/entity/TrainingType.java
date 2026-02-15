package com.epam.gym.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.util.UUID;

@Getter
@Setter
@ToString
@NoArgsConstructor
// TODO:
//  [Optional]
//  If TrainingType represents a fixed set of predefined types (e.g., "CARDIO", "STRENGTH", "YOGA"),
//  using an enum is preferable. Enums provide type safety, are easier to maintain,
//  make the code more readable and are faster to compare than strings.
//  You can keep the strings approach though - it also works here.
public class TrainingType {
    private String id;
    private String trainingType;
    public TrainingType( String trainingType) {
        this.trainingType = trainingType;
        id = UUID.randomUUID().toString();
    }
}
