package com.epam.gym.controller;

import com.epam.gym.dto.*;
import com.epam.gym.service.TrainerService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/v1/trainer")
@Tag(name = "Trainer Api list", description = "this api is for Trainer")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class TrainerController {

    private final TrainerService trainerService;

    //1. Trainer Registration (POST method)
    @PostMapping("/public")
    @Operation(summary = "Create for Trainer", description = "")
    public ResponseEntity<ApiResponse<AuthDTO>> createTrainer(
            @Valid @RequestBody CreateTrainerRequestDTO dto) {
        return ResponseEntity.ok(trainerService.createTrainer(dto));
    }

    //    8. Get Trainer Profile (GET method)
    @GetMapping()
    @Operation(summary = "Get Trainer", description = "")
    public ResponseEntity<ApiResponse<TrainerDTO>> getTrainer(
            @RequestParam(name = "username") String username) {
        return ResponseEntity.ok(trainerService.getTrainerByUsername(username));
    }

    //    9. Update Trainer Profile (PUT method)
    @PutMapping()
    @Operation(summary = "Update Trainer details ", description = "")
    public ResponseEntity<ApiResponse<TrainerDTO>> updateTrainerDetails(@Valid @RequestBody UpdateTrainerRequestDTO dto) {
        return ResponseEntity.ok(trainerService.updateTrainer(dto));
    }

    //    10. Get not assigned on trainee active trainers. (GET method)
    @GetMapping("/not-assigned-on-trainee")
    @Operation(summary = "Get not assigned on trainee active trainers.  ", description = "")
    public ResponseEntity<ApiResponse<List<TrainerDTO>>> getTrainerNotAssignedOnTrainee(
            @RequestParam(name = "username") String username) {
        return ResponseEntity.ok(trainerService.getTrainersNotAssignedOnTrainee(username));
    }

    //    16. Activate/De-Activate Trainer (PATCH method)
    @PatchMapping("/is-active")
    @Operation(summary = " 15. Activate/De-Activate Trainer ", description = "")
    public ResponseEntity<ApiResponse<?>> changeStatusTrainer(
            @RequestBody ChangeStatusRequestDTO dto) {
        return ResponseEntity.ok(trainerService.changeStatusTrainee(dto));
    }


}
