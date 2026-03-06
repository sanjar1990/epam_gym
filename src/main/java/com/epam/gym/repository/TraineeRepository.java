package com.epam.gym.repository;

import com.epam.gym.entity.Trainee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

// TODO:
//  For this and for TrainerRepository:
//  1. Remove unused methods here and check other places with 'Inspect Code'.
//  2. Can Spring Data JPA generate these methods by their names without @Query?
@Repository
public interface TraineeRepository extends JpaRepository<Trainee, Long> {

    Optional<Trainee> findByUserUsername(String username);
}
