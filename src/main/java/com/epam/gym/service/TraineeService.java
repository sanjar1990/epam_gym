package com.epam.gym.service;

import com.epam.gym.dao.TraineeDao;
import com.epam.gym.entity.Trainee;
import com.epam.gym.util.UsernamePasswordGenerator;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.UUID;

@Service
@Getter
@Slf4j
public class TraineeService {



    private TraineeDao traineeDao;
    private UsernamePasswordGenerator generator;

    @Autowired
    public void setTraineeDao(TraineeDao traineeDao) {
        this.traineeDao = traineeDao;
    }

    @Autowired
    public void setGenerator(UsernamePasswordGenerator generator) {
        this.generator = generator;
    }

    public Trainee create(Trainee trainee) {

        String username = generator.generateUsername(
                trainee.getFirstName(),
                trainee.getLastName(),
                traineeDao.findAll()
        );

        trainee.setUsername(username);
        trainee.setPassword(generator.generatePassword());
        trainee.setActive(true);
        trainee.setId(UUID.randomUUID().toString());

        traineeDao.save(trainee);

        log.info("Created trainee {}", username);

        return trainee;
    }

    public Trainee find(String id) {
        return traineeDao.findById(id);
    }

    public void update(Trainee trainee) {
        traineeDao.save(trainee);
        log.info("Updated trainee {}", trainee.getUsername());
    }

    public void delete(String id) {
        traineeDao.getStorage().getTraineeMap().remove(id);
        log.info("Deleted trainee id={}", id);
    }
}
