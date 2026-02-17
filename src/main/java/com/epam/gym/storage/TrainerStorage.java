package com.epam.gym.storage;

import com.epam.gym.entity.Trainer;
import lombok.Getter;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Getter
@Component
public class TrainerStorage {

    private final Map<Long, Trainer> trainerMap = new HashMap<>();

}