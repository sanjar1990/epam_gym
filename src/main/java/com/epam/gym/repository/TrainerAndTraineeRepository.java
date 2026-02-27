package com.epam.gym.repository;

import com.epam.gym.entity.TrainerAndTrainee;
import com.epam.gym.mapper.TrainerMapperI;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface TrainerAndTraineeRepository extends JpaRepository<TrainerAndTrainee, Long> {
    @Query("SELECT tt.trainerId as trainerId, tru.firstName as firstName, tru.lastName as lastName, " +
            "tru.username as trainerUsername, tru.isActive as isActive, tp.trainingTypeName as trainingTypeName," +
            "tp.id as trainingTypeId    from TrainerAndTrainee tt  " +
            "inner join Trainer as tr on tt.trainerId = tr.id " +
            "inner join User as tru on tr.user.id = tru.id " +
            "inner join TrainingType as tp on tr.trainingTypeId = tp.id " +
            "inner join Trainee as te on tt.traineeId = te.id " +
            "where te.user.username != :traineeUsername and tru.isActive = true ")
    List<TrainerMapperI> findTrainersNotAssignedOnTrainee(@Param("traineeUsername") String traineeUsername);
}
