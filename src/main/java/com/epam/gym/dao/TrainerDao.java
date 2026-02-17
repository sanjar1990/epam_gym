package com.epam.gym.dao;

import com.epam.gym.entity.Trainer;
import com.epam.gym.entity.User;
import com.epam.gym.exceptions.UsernameExistsException;
import com.epam.gym.storage.TrainerStorage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.Collection;

@Repository
public class TrainerDao {

    private TrainerStorage storage;

    @Autowired
    public void setStorage(TrainerStorage storage) {
        this.storage = storage;
    }

    public void save(Trainer trainer) {
        storage.getTrainerMap().put(trainer.getId(), trainer);
    }

    public Trainer findById(Long id) {
        return storage.getTrainerMap().get(id);
    }

    public Collection<Trainer> findAll() {
        return storage.getTrainerMap().values();
    }

    public void update(Trainer trainer, Collection<User> existingUsers) {
        boolean exists = existingUsers.stream()
                .anyMatch(u -> u.getUsername().startsWith(trainer.getUsername()));
        if (exists) {
            throw new UsernameExistsException("Trainer with username " + trainer.getUsername() + " already exists");
        }
    }
}
