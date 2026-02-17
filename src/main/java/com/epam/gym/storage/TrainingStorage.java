package com.epam.gym.storage;

import com.epam.gym.entity.Training;
import lombok.Getter;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Getter
@Component
public class TrainingStorage {
    private final Map<Long, Training> trainningMap = new HashMap<>();
}
