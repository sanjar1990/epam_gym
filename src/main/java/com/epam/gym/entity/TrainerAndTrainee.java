package com.epam.gym.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "trainer_and_trainee")
public class TrainerAndTrainee {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "trainer_id")
    private Long trainerId;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "trainer_id", insertable = false, updatable = false)
    private Trainer trainer;
    @Column(name = "trainee_id")
    private Long traineeId;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "trainee_id", insertable = false, updatable = false)
    private Trainee trainee;
}
