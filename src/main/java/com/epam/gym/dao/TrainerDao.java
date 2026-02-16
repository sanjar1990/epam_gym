package com.epam.gym.dao;

import com.epam.gym.entity.Trainer;
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

    public Trainer findById(String id) {
        return storage.getTrainerMap().get(id);
    }

    public Collection<Trainer> findAll() {
        return storage.getTrainerMap().values();
    }
}
