package com.epam.gym.service;

import com.epam.gym.dao.TrainingDao;
import com.epam.gym.entity.Training;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class TrainingService {
    private TrainingDao trainingDao;
    @Autowired
    public void setTrainingDao(TrainingDao trainingDao) {
        this.trainingDao = trainingDao;
    }
    public void create(Training training) {

        training.setId(UUID.randomUUID().toString());

        trainingDao.save(training);
    }

    public Training find(String id) {
        return trainingDao.findById(id);
    }
}
