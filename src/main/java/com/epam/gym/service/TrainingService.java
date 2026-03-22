package com.epam.gym.service;

import com.epam.gym.dto.*;
import com.epam.gym.entity.Trainee;
import com.epam.gym.entity.Trainer;
import com.epam.gym.entity.Training;
import com.epam.gym.mapper.training.TrainingMapperI;
import com.epam.gym.repository.TrainingRepository;
import com.epam.gym.specification.TrainingSpecification;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
public class TrainingService {
    private TrainingRepository trainingRepository;
    private TraineeService traineeService;
    private TrainerService trainerService;
    private TrainingMapperI trainingMapperI;

    @Autowired
    public void setTraineeService(TraineeService traineeService) {
        this.traineeService = traineeService;
    }

    @Autowired
    public void setTrainerService(TrainerService trainerService) {
        this.trainerService = trainerService;
    }

    @Autowired
    public void setTrainingRepository(TrainingRepository trainingRepository) {
        this.trainingRepository = trainingRepository;
    }
    @Autowired
    public void setTrainingMapper(TrainingMapperI trainingMapperI) {
        this.trainingMapperI = trainingMapperI;
    }
    //16. Add training.
    @Transactional
    public void addTraining(CreateTrainingDTO dto) {


        Trainee trainee = traineeService.getTrainee(dto.getTraineeUsername());
        Trainer trainer = trainerService.getTrainerEntityByUsername(dto.getTrainerUsername());
        //Check
        if (!trainer.getTrainingType().getId().equals(dto.getTrainingTypeId())) {
            throw new RuntimeException("Training type id is not match");
        }
        Training training = trainingMapperI.toEntity(dto);

        training.setTrainee(trainee);
        training.setTrainer(trainer);

        trainee.getTrainings().add(training);
        trainee.getTrainers().add(trainer);
        trainer.getTrainees().add(trainee);

        trainingRepository.save(training);
        log.info("Training added {}", training.getId());
    }

    //    14. Get Trainee Trainings List by trainee username and criteria
//            (from date, to date, trainer name, training type).
    public List<TraineeTrainingResponseDTO> getTrainingsByTraineeUsernameCriteria(
            GetTraineeTrainingsCriteriaFilterDTO dto) {
        Specification<Training> spec = TrainingSpecification.filterByCriteriaForTrainee(dto);

        return trainingRepository.findAll(spec)
                .stream()
                .map(trainingMapperI::toTraineeTrainingResponseDTO)
                .toList();
    }

    //    15. Get Trainer Trainings List by trainer username and criteria (from date, to date, trainee name).
    public List<TrainerTrainingResponseDTO> getTrainingsByTrainerUsernameCriteria(
            GetTrainerTrainingsCriteriaFilterDTO dto) {
        Specification<Training> spec = TrainingSpecification.filterByCriteriaForTrainer(dto);
        return trainingRepository.findAll(spec)
                .stream()
                .map(trainingMapperI::toTrainerTrainingResponseDTO)
                .toList();
    }

}
