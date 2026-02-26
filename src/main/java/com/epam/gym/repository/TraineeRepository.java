package com.epam.gym.repository;

import com.epam.gym.entity.Trainee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TraineeRepository extends JpaRepository<Trainee,Long> {

   @Query("SELECT t FROM Trainee t WHERE t.user.username = ?1 AND t.user.password = ?2")
    Optional<Trainee> findByUsernameAndPassword(String username, String password);
    @Query("SELECT t FROM Trainee t WHERE t.user.username = ?1")
    Optional<Trainee> findByUsername(String username);
}
