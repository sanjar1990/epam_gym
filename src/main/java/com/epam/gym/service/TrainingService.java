package com.epam.gym.service;

import com.epam.gym.dto.*;
import com.epam.gym.entity.Trainee;
import com.epam.gym.entity.Trainer;
import com.epam.gym.entity.Training;
import com.epam.gym.entity.User;
import com.epam.gym.mapper.trainee.TraineeMapper;
import com.epam.gym.mapper.trainer.TrainerMapper;
import com.epam.gym.mapper.training_type.TrainingTypeMapper;
import com.epam.gym.repository.TrainingRepository;
import com.epam.gym.specification.TrainingSpecification;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class TrainingService {
    private TrainingRepository trainingRepository;
    private AuthService authService;
    private TraineeService traineeService;
    private TrainingTypeService trainingTypeService;
    private TrainerAndTraineeService trainerAndTraineeService;

    @Autowired
    public void setTrainerAndTraineeService(TrainerAndTraineeService trainerAndTraineeService) {
        this.trainerAndTraineeService = trainerAndTraineeService;
    }
    @Autowired
    public void setTrainingTypeService(TrainingTypeService trainingTypeService) {
        this.trainingTypeService = trainingTypeService;
    }
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
        trainerAndTraineeService.addTrainerAndTrainee( dto.getTrainerId(),trainee.getId());
    }
    //    14. Get Trainee Trainings List by trainee username and criteria
//            (from date, to date, trainer name, training type).
    public List<TrainingResponseDTO> getTrainingsByTraineeUsernameCriteria(
            GetTraineeTrainingsCriteriaFilterDTO dto) {
        Specification<Training> spec = TrainingSpecification.filterByCriteriaForTrainee(dto);

       return trainingRepository.findAll(spec)
                .stream()
                .map(this::toTrainingResponseDTO)
                .toList();
    }

//    15. Get Trainer Trainings List by trainer username and criteria (from date, to date, trainee name).
public List<TrainingResponseDTO> getTrainingsByTrainerUsernameCriteria(
        GetTrainerTrainingsCriteriaFilterDTO dto) {
    Specification<Training> spec = TrainingSpecification.filterByCriteriaForTrainer(dto);

    return trainingRepository.findAll(spec)
            .stream()
            .map(this::toTrainingResponseDTO)
            .toList();
}



    private TrainingResponseDTO toTrainingResponseDTO(Training training) {
        TrainingResponseDTO trainingResponseDTO = new TrainingResponseDTO();
        trainingResponseDTO.setId(training.getId());
        trainingResponseDTO.setTrainee(TraineeMapper.toTraineeDTO(training.getTrainee()));
        trainingResponseDTO.setTrainer(TrainerMapper.toTrainerDTO(training.getTrainer()));
        trainingResponseDTO.setTrainingName(training.getTrainingName());
        trainingResponseDTO.setTrainingType(TrainingTypeMapper
                .toTrainingTypeDTO(training.getTrainingType()));
        trainingResponseDTO.setTrainingDate(training.getTrainingDate());
        trainingResponseDTO.setTrainingDuration(training.getTrainingDuration());
        return trainingResponseDTO;
    }






}
