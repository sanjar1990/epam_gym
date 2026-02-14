package com.epam.gym.service;

import com.epam.gym.dao.TrainerDao;
import com.epam.gym.entity.Trainer;
import com.epam.gym.util.UsernamePasswordGenerator;
import lombok.Setter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Setter
@Service
public class TrainerService {
    private static final Logger log =
            LoggerFactory.getLogger(TrainerService.class);

    private TrainerDao trainerDao;
    private UsernamePasswordGenerator generator;

    @Autowired
    public void setTrainerDao(TrainerDao trainerDao) {
        this.trainerDao = trainerDao;
    }

    @Autowired
    public void setGenerator(UsernamePasswordGenerator generator) {
        this.generator = generator;
    }

    public Trainer create(Trainer trainer) {

        String username = generator.generateUsername(
                trainer.getFirstName(),
                trainer.getLastName(),
                trainerDao.findAll()
        );

        trainer.setUsername(username);
        trainer.setPassword(generator.generatePassword());
        trainer.setActive(true);
        trainer.setId(UUID.randomUUID().toString());

        trainerDao.save(trainer);

        log.info("Created trainer {}", username);

        return trainer;
    }

    public Trainer find(String id) {
        return trainerDao.findById(id);
    }

    public void update(Trainer trainer) {
        trainerDao.save(trainer);
        log.info("Updated trainer {}", trainer.getUsername());
    }
}
