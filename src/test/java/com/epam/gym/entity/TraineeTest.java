package com.epam.gym.entity;

import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.HashSet;

import static org.junit.jupiter.api.Assertions.*;

class TraineeTest {

    @Test
    void addTrainer_shouldAddBothSides() {
        // Arrange
        Trainee trainee = new Trainee();
        Trainer trainer = new Trainer();

        trainee.setTrainers(new HashSet<>());
        trainer.setTrainees(new HashSet<>());

        // Act
        trainee.addTrainer(trainer);

        // Assert
        assertTrue(trainee.getTrainers().contains(trainer));
        assertTrue(trainer.getTrainees().contains(trainee));
    }

    @Test
    void removeTrainer_shouldRemoveBothSides() {
        // Arrange
        Trainee trainee = new Trainee();
        Trainer trainer = new Trainer();

        trainee.setTrainers(new HashSet<>());
        trainer.setTrainees(new HashSet<>());

        trainee.addTrainer(trainer);

        // Act
        trainee.removeTrainer(trainer);

        // Assert
        assertFalse(trainee.getTrainers().contains(trainer));
        assertFalse(trainer.getTrainees().contains(trainee));
    }

    @Test
    void settersAndGetters_shouldWorkCorrectly() {
        // Arrange
        Trainee trainee = new Trainee();
        User user = new User();

        LocalDate dob = LocalDate.of(2000, 1, 1);
        String address = "Seoul";

        // Act
        trainee.setDateOfBirth(dob);
        trainee.setAddress(address);
        trainee.setUser(user);

        // Assert
        assertEquals(dob, trainee.getDateOfBirth());
        assertEquals(address, trainee.getAddress());
        assertEquals(user, trainee.getUser());
    }
}