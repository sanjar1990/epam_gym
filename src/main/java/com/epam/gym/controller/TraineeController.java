package com.epam.gym.controller;

import com.epam.gym.dto.*;
import com.epam.gym.service.TraineeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/v1/trainee")
@Tag(name = "Trainee Api list", description = "this api is for Trainee")
@RequiredArgsConstructor()
public class TraineeController {

    private final TraineeService traineeService;

    //1. Trainee Registration (POST method)
    @PostMapping()
    @Operation(summary = "Create for Trainee", description = "")
    public ResponseEntity<AuthDTO> createTrainee(@Valid @RequestBody CreateTraineeRequestDTO dto) {
        return ResponseEntity.ok(traineeService.createTrainee(dto));
    }

    // TODO:
    //  [Optional]
    //  It is not a big deal, but could be more RESTful with GET /api/v1/trainee/{username} instead of query param
    //  Same applies to other HTTP verbs, you approach works though.
    //  Just remember to think of your data as a resource when working in REST methodology

    //    5. Get Trainee Profile (GET method)
    @GetMapping("/{username}")
    @Operation(summary = "Get Trainee", description = "")
    public ResponseEntity<TraineeDTO> getTrainee(@PathVariable(name = "username") String username) {
        return ResponseEntity.ok(traineeService.getTraineeByUsername(username));
    }

    //    6. Update Trainee Profile (PUT method)
    @PutMapping()
    @Operation(summary = "Update Trainee details ", description = "")
    public ResponseEntity<TraineeDTO> updateTraineeDetails(@Valid @RequestBody UpdateTraineeRequestDTO dto) {
        return ResponseEntity.ok(traineeService.updateTrainee(dto));
    }

    //    7. Delete Trainee Profile (DELETE method)
    @DeleteMapping()
    @Operation(summary = "Delete Trainee ", description = "")
    @ResponseStatus(HttpStatus.OK)
    public void deleteTrainee(@RequestParam(name = "username") String username) {
        traineeService.deleteTrainee(username);
    }

    // TODO:
    //  [Optional]
    //  Can be more RESTful with PUT /api/v1/trainee/{username}/trainers and PATCH /api/v1/trainee/{username}/active
    //  An HTTP verb already suggests the action, so no need to add it in the path.
    //  Again not a strict requirement but verbs in paths are more RPC-style API, than REST

    //    11. Update Trainee's Trainer List (PUT method)
    @PutMapping("/{username}/trainers")
    @Operation(summary = "Update trainee's trainer list")
    public ResponseEntity<List<TrainerDTO>> updateTrainerList(
            @PathVariable String username,
            @RequestBody UpdateTrainersRequestDTO dto) {

        return ResponseEntity.ok(
                traineeService.updateTrainerList(username, dto)
        );
    }

    //    15. Activate/De-Activate Trainee (PATCH method)
    @PatchMapping("/active")
    @Operation(summary = " 15. Activate/De-Activate Trainee ", description = "")
    @ResponseStatus(HttpStatus.OK)
    public void changeStatusTrainee(@RequestBody ChangeStatusRequestDTO dto) {
        traineeService.changeStatusTrainee(dto);
    }
}
