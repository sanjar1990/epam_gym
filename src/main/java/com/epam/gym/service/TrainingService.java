package com.epam.gym.service;

import com.epam.gym.dto.*;
import com.epam.gym.entity.Trainee;
import com.epam.gym.entity.Trainer;
import com.epam.gym.entity.Training;
import com.epam.gym.entity.UserRole;
import com.epam.gym.enums.ActionType;
import com.epam.gym.mapper.training.TrainingMapperI;
import com.epam.gym.repository.TrainingRepository;
import com.epam.gym.service.clint.WorkloadClientService;
import com.epam.gym.specification.TrainingSpecification;
import io.micrometer.core.annotation.Counted;
import io.micrometer.core.annotation.Timed;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class TrainingService {
    private TrainingRepository trainingRepository;
    private TraineeService traineeService;
    private TrainerService trainerService;
    private TrainingMapperI trainingMapperI;
    private final WorkloadClientService workloadClientService;
    private final JwtTokenService jwtTokenService;

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
    @Timed(value = "training.create.time", description = "Time to create training")
    @Counted(value = "training.create.count", description = "Count training creation")
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
        toWorkloadService(trainer, training, ActionType.ADD);
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

    public Long getTrainingsCount() {
        return trainingRepository.count();
    }

    public void deleteTraining(Long trainingId) {
        Training training = trainingRepository.findById(trainingId)
                .orElseThrow(() -> new RuntimeException("Training not found"));
        trainingRepository.deleteById(trainingId);
        toWorkloadService(training.getTrainer(), training, ActionType.DELETE);
        log.info("Training deleted {}", trainingId);
    }

    private void toWorkloadService(Trainer trainer, Training training, ActionType actionType) {
        TrainerWorkloadRequest trainerWorkloadRequest = new TrainerWorkloadRequest();
        trainerWorkloadRequest.setUsername(trainer.getUser().getUsername());
        trainerWorkloadRequest.setFirstName(trainer.getUser().getFirstName());
        trainerWorkloadRequest.setLastName(trainer.getUser().getLastName());
        trainerWorkloadRequest.setTrainingDate(training.getTrainingDate());
        trainerWorkloadRequest.setTrainingDuration(training.getTrainingDuration());
        trainerWorkloadRequest.setIsActive(trainer.getUser().getIsActive());
        trainerWorkloadRequest.setActionType(actionType);
        String token = jwtTokenService.encode(trainer.getUser().getUsername(),
                trainer.getUser().getRoles().stream().map(UserRole::getRole).collect(Collectors.toList()));
        workloadClientService.sendWorkload(trainerWorkloadRequest, "Bearer " + token, MDC.get("transactionId"));
    }


}
