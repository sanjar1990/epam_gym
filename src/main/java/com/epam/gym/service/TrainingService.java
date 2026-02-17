package com.epam.gym.service;

import com.epam.gym.dao.TrainingDao;
import com.epam.gym.entity.Training;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

// TODO:
//  [Optional]
//  This and some other files are not formatted. You can format them using your IDE's code formatting features.
//  Even though formatting could be considered a minor issue, it can significantly improve the readability of the code.
//  Also note that some teams/customers can be more strict about formatting and such file would not pass code review
@Service
public class TrainingService {
    private TrainingDao trainingDao;

    @Autowired
    public void setTrainingDao(TrainingDao trainingDao) {
        this.trainingDao = trainingDao;
    }

    public void create(Training training) {
        trainingDao.save(training);
    }

    public Training find(Long id) {
        return trainingDao.findById(id);
    }
}
