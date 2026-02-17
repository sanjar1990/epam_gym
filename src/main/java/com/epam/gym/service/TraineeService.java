package com.epam.gym.service;

import com.epam.gym.dao.TraineeDao;
import com.epam.gym.dao.TrainerDao;
import com.epam.gym.entity.Trainee;
import com.epam.gym.entity.User;
import com.epam.gym.util.PasswordGenerator;
import com.epam.gym.util.UsernameGenerator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;

@Service
@Slf4j
public class TraineeService {

    // TODO
    //  You have @Slf4j annotation in the project, consider using it throughout the app
    //  instead of creating a logger manually.
    // DONE

    private TraineeDao traineeDao;
    private TrainerDao trainerDao;
    private PasswordGenerator passwordGenerator;
    private UsernameGenerator usernameGenerator;

    @Autowired
    public void setTraineeDao(TraineeDao traineeDao) {
        this.traineeDao = traineeDao;
    }

    @Autowired
    public void setPasswordGenerator(PasswordGenerator passwordGenerator) {
        this.passwordGenerator = passwordGenerator;
    }

    @Autowired
    public void setUsernameGenerator(UsernameGenerator usernameGenerator) {
        this.usernameGenerator = usernameGenerator;
    }

    // TODO
    //  This is not stated clearly in the task, but will be important later in the course:
    //  Since both Trainer and Trainee extend User class - "generateUsername" method should check
    //  both storages at once for the existing username.
    public Trainee create(Trainee trainee) {
        trainerDao.findAll();

        String username = usernameGenerator.generateUsername(
                trainee.getFirstName(),
                trainee.getLastName(),
                getAllUsers()
        );

        trainee.setUsername(username);
        trainee.setPassword(passwordGenerator.generatePassword());
        trainee.setActive(true);
        traineeDao.save(trainee);

        log.info("Created trainee {}", username);

        return trainee;
    }

    public Trainee find(Long id) {
        return traineeDao.findById(id);
    }

    // TODO
    //  What if the trainee/trainer username was changed to something that already exists in the storage?
    //  Consider adding a check for that in the "update" method and throw an exception if the username is not unique
    // Done
    public void update(Trainee trainee) {
        traineeDao.update(trainee, getAllUsers());
        log.info("Updated trainee {}", trainee.getUsername());
    }

    public void delete(Long id) {
        traineeDao.remove(id);
        log.info("Deleted trainee id={}", id);
    }

    private Collection<User> getAllUsers() {
        Collection<User> users = new ArrayList<>();
        users.addAll(traineeDao.findAll());
        users.addAll(trainerDao.findAll());
        return users;
    }
}
