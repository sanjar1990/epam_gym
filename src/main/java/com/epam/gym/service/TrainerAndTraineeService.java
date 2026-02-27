package com.epam.gym.service;

import com.epam.gym.dto.TrainerDTO;
import com.epam.gym.entity.TrainerAndTrainee;
import com.epam.gym.mapper.trainer.TrainerMapper;
import com.epam.gym.repository.TrainerAndTraineeRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
@Slf4j
@Service
public class TrainerAndTraineeService {
    private final TrainerAndTraineeRepository trainerAndTraineeRepository;

    public TrainerAndTraineeService(TrainerAndTraineeRepository trainerAndTraineeRepository) {
        this.trainerAndTraineeRepository = trainerAndTraineeRepository;
    }

    public void addTrainerAndTrainee(Long trainerId, Long traineeId) {
        TrainerAndTrainee trainerAndTrainee = new TrainerAndTrainee();
        trainerAndTrainee.setTrainerId(trainerId);
        trainerAndTrainee.setTraineeId(traineeId);
        trainerAndTraineeRepository.save(trainerAndTrainee);
    }

    public List<TrainerDTO> getTrainersNotAssignedOnTrainee(String traineeUsername) {
       return trainerAndTraineeRepository.findTrainersNotAssignedOnTrainee(traineeUsername)
               .stream()
               .map(TrainerMapper::toTrainerDTO)
               .toList();
    }



}
