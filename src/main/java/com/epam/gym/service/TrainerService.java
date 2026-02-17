package com.epam.gym.service;

import com.epam.gym.dao.TraineeDao;
import com.epam.gym.dao.TrainerDao;
import com.epam.gym.entity.Trainer;
import com.epam.gym.entity.User;
import com.epam.gym.util.PasswordGenerator;
import com.epam.gym.util.UsernameGenerator;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;

@Setter
@Service
@Slf4j
public class TrainerService {


    private TrainerDao trainerDao;
    private TraineeDao traineeDao;
    private PasswordGenerator passwordGenerator;
    private UsernameGenerator usernameGenerator;

    @Autowired
    public void setTrainerDao(TrainerDao trainerDao) {
        this.trainerDao = trainerDao;
    }
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

    public Trainer create(Trainer trainer) {

        String username = usernameGenerator.generateUsername(
                trainer.getFirstName(),
                trainer.getLastName(),
                getAllUsers()
        );

        trainer.setUsername(username);
        trainer.setPassword(passwordGenerator.generatePassword());
        trainer.setActive(true);

        trainerDao.save(trainer);

        log.info("Created trainer {}", username);

        return trainer;
    }

    public Trainer find(Long id) {
        return trainerDao.findById(id);
    }

    public void update(Trainer trainer) {
        trainerDao.update(trainer, getAllUsers());
        log.info("Updated trainer {}", trainer.getUsername());
    }
    private Collection<User> getAllUsers() {
        Collection<User> users = new ArrayList<>();
        users.addAll(traineeDao.findAll());
        users.addAll(trainerDao.findAll());
        return users;
    }
}
