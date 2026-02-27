package com.epam.gym.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@Entity
public class Training {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "trainee_id")
    private Long traineeId;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "trainee_id", insertable = false, updatable = false)
//    @OnDelete(action = OnDeleteAction.CASCADE)
    private Trainee trainee;
    @Column(name = "trainer_id")
    private Long trainerId;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "trainer_id", insertable = false, updatable = false)
    private Trainer trainer;

    @Column(nullable = false)
    private String trainingName;
    @Column(name = "training_type_id")
    private Long trainingTypeId;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "training_type_id", insertable = false, updatable = false)
    private TrainingType trainingType;

    @Column(nullable = false)
    private LocalDate trainingDate;

    @Column(nullable = false)
    private Integer trainingDuration;
}
