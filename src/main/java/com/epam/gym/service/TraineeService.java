package com.epam.gym.service;

import com.epam.gym.dao.TraineeDao;
import com.epam.gym.entity.Trainee;
import com.epam.gym.util.UsernamePasswordGenerator;
import lombok.Getter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@Getter
public class TraineeService {

    // TODO
    //  You have @Slf4j annotation in the project, consider using it throughout the app
    //  instead of creating a logger manually.
    private static final Logger log =
            LoggerFactory.getLogger(TraineeService.class);

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

    // TODO
    //  This is not stated clearly in the task, but will be important later in the course:
    //  Since both Trainer and Trainee extend User class - "generateUsername" method should check
    //  both storages at once for the existing username.
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

    // TODO
    //  What if the trainee/trainer username was changed to something that already exists in the storage?
    //  Consider adding a check for that in the "update" method and throw an exception if the username is not unique
    public void update(Trainee trainee) {
        traineeDao.save(trainee);
        log.info("Updated trainee {}", trainee.getUsername());
    }

    public void delete(String id) {
        traineeDao.getStorage().getTraineeMap().remove(id);
        log.info("Deleted trainee id={}", id);
    }
}
