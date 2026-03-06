package com.epam.gym.service;

import com.epam.gym.dto.CreateTrainingDTO;
import com.epam.gym.dto.GetTraineeTrainingsCriteriaFilterDTO;
import com.epam.gym.dto.GetTrainerTrainingsCriteriaFilterDTO;
import com.epam.gym.dto.TrainingResponseDTO;
import com.epam.gym.entity.Trainee;
import com.epam.gym.entity.Training;
import com.epam.gym.mapper.training.TrainingMapper;
import com.epam.gym.repository.TrainingRepository;
import com.epam.gym.specification.TrainingSpecification;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
public class TrainingService {
    private TrainingRepository trainingRepository;
    private AuthService authService;
    private TraineeService traineeService;

    @Autowired
    public void setAuthService(AuthService authService) {
        this.authService = authService;
    }

    @Autowired
    public void setTraineeService(TraineeService traineeService) {
        this.traineeService = traineeService;
    }

    @Autowired
    public void setTrainingRepository(TrainingRepository trainingRepository) {
        this.trainingRepository = trainingRepository;
    }

    //16. Add training.
    public void addTraining(String traineeUsername, String traineePassword, CreateTrainingDTO dto) {
        authService.login(traineeUsername, traineePassword);
        Trainee trainee = traineeService.getTraineeByUsername(traineeUsername);
        Training training = new Training();
        training.setTrainee(trainee);
        training.setTrainerId(dto.getTrainerId());
        training.setTrainingName(dto.getTrainingName());
        training.setTrainingTypeId(dto.getTrainingTypeId());
        training.setTrainingDate(dto.getTrainingDate());
        training.setTrainingDuration(dto.getTrainingDuration());
        trainingRepository.save(training);
        log.info("Training added {}", training.getId());
    }

    //    14. Get Trainee Trainings List by trainee username and criteria
//            (from date, to date, trainer name, training type).
    public List<TrainingResponseDTO> getTrainingsByTraineeUsernameCriteria(
            GetTraineeTrainingsCriteriaFilterDTO dto) {
        Specification<Training> spec = TrainingSpecification.filterByCriteriaForTrainee(dto);

        return trainingRepository.findAll(spec)
                .stream()
                .map(TrainingMapper::toTrainingResponseDTO)
                .toList();
    }

    //    15. Get Trainer Trainings List by trainer username and criteria (from date, to date, trainee name).
    public List<TrainingResponseDTO> getTrainingsByTrainerUsernameCriteria(
            GetTrainerTrainingsCriteriaFilterDTO dto) {
        Specification<Training> spec = TrainingSpecification.filterByCriteriaForTrainer(dto);

        return trainingRepository.findAll(spec)
                .stream()
                .map(TrainingMapper::toTrainingResponseDTO)
                .toList();
    }


    //18. Update Tranee's trainers list
    public void updateTrainerList(String traineeUsername, String traineePassword,
                                  Long trainerId, Long trainingId) {
        authService.login(traineeUsername, traineePassword);
        Optional<Training> optTraining = trainingRepository.findById(trainingId);
        if (optTraining.isEmpty()) throw new RuntimeException("Training not found");
        Training training = optTraining.get();
        training.setTrainerId(trainerId);
        trainingRepository.save(training);
        log.info("Training updated {}", training.getId());
    }

}
