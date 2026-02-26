package com.epam.gym.repository;

import com.epam.gym.entity.Trainer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TrainerRepository extends JpaRepository<Trainer, Long> {
    @Query("SELECT t FROM Trainer t WHERE t.user.username = ?1 AND t.user.password = ?2")
    Optional<Trainer> findByUsernameAndPassword(String username, String password);
    @Query("SELECT t FROM Trainer t WHERE t.user.username = ?1")
    Optional<Trainer> findByUserUsername(String username);
}



