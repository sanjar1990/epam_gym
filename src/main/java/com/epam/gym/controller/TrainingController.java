package com.epam.gym.controller;

import com.epam.gym.dto.*;
import com.epam.gym.service.TrainingService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/v1/training")
@Tag(name = "Training Api list", description = "this api is for Training")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class TrainingController {
    private final TrainingService trainingService;

    //    12. Get Trainee Trainings List (GET method)
    @PostMapping("/trainee")
    @Operation(summary = "Get Trainee Trainings List", description = "")
    public ResponseEntity<ApiResponse<List<TraineeTrainingResponseDTO>>> getTraineeTrainings(
            @RequestBody GetTraineeTrainingsCriteriaFilterDTO dto) {
        return ResponseEntity.ok(trainingService.getTrainingsByTraineeUsernameCriteria(dto));
    }

    //    13. Get Trainer Trainings List (GET method)
    @PostMapping("/trainer")
    @Operation(summary = "Get Trainer Trainings List", description = "")
    public ResponseEntity<ApiResponse<List<TrainerTrainingResponseDTO>>> getTrainerTrainings(
            @RequestBody GetTrainerTrainingsCriteriaFilterDTO dto) {
        return ResponseEntity.ok(trainingService.getTrainingsByTrainerUsernameCriteria(dto));
    }

    //    14 Add Training (POST method)
    @PostMapping("/add-training")
    @Operation(summary = " 14 Add Training", description = "")
    public ResponseEntity<ApiResponse<?>> addTraining(@Valid @RequestBody CreateTrainingDTO dto) {
        return ResponseEntity.ok(trainingService.addTraining(dto));
    }


}
