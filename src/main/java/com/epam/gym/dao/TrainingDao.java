package com.epam.gym.dao;

import com.epam.gym.entity.Training;
import com.epam.gym.storage.TrainingStorage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.Collection;

@Repository
public class TrainingDao {
    private TrainingStorage storage;

    @Autowired
    public void setStorage(TrainingStorage storage) {
        this.storage = storage;
    }

    public void save(Training training) {
        storage.getTrainningMap().put(training.getId(), training);
    }

    public Training findById(Long id) {
        return storage.getTrainningMap().get(id);
    }

    public Collection<Training> findAll() {
        return storage.getTrainningMap().values();
    }
}
