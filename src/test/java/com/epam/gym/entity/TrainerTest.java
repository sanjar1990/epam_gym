package com.epam.gym.entity;

import org.junit.jupiter.api.Test;

import java.util.HashSet;

import static org.junit.jupiter.api.Assertions.*;

class TrainerTest {

    @Test
    void constructor_shouldInitializeTraineesSet() {
        // Act
        Trainer trainer = new Trainer();

        // Assert
        assertNotNull(trainer.getTrainees());
        assertTrue(trainer.getTrainees().isEmpty());
    }

    @Test
    void settersAndGetters_shouldWorkCorrectly() {
        // Arrange
        Trainer trainer = new Trainer();
        User user = new User();
        TrainingType trainingType = new TrainingType();

        Long trainingTypeId = 1L;

        // Act
        trainer.setUser(user);
        trainer.setTrainingTypeId(trainingTypeId);
        trainer.setTrainingType(trainingType);

        // Assert
        assertEquals(user, trainer.getUser());
        assertEquals(trainingTypeId, trainer.getTrainingTypeId());
        assertEquals(trainingType, trainer.getTrainingType());
    }

    @Test
    void traineesRelationship_shouldAddTrainee() {
        // Arrange
        Trainer trainer = new Trainer();
        Trainee trainee = new Trainee();

        trainer.setTrainees(new HashSet<>());

        // Act
        trainer.getTrainees().add(trainee);

        // Assert
        assertTrue(trainer.getTrainees().contains(trainee));
    }

    @Test
    void traineesRelationship_shouldRemoveTrainee() {
        // Arrange
        Trainer trainer = new Trainer();
        Trainee trainee = new Trainee();

        trainer.setTrainees(new HashSet<>());
        trainer.getTrainees().add(trainee);

        // Act
        trainer.getTrainees().remove(trainee);

        // Assert
        assertFalse(trainer.getTrainees().contains(trainee));
    }

    @Test
    void bidirectionalRelationship_shouldBeConsistent_whenManuallyHandled() {
        // Arrange
        Trainer trainer = new Trainer();
        Trainee trainee = new Trainee();

        trainer.setTrainees(new HashSet<>());
        trainee.setTrainers(new HashSet<>());

        // Act (manual sync, since Trainer has no helper method)
        trainer.getTrainees().add(trainee);
        trainee.getTrainers().add(trainer);

        // Assert
        assertTrue(trainer.getTrainees().contains(trainee));
        assertTrue(trainee.getTrainers().contains(trainer));
    }
}