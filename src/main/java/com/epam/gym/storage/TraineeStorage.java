package com.epam.gym.storage;

import com.epam.gym.entity.Trainee;
import lombok.Getter;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Getter
@Component
public class TraineeStorage {
    private final Map<Long, Trainee> traineeMap = new HashMap<>();

}
