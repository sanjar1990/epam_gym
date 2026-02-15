package com.epam.gym.dao;

import com.epam.gym.entity.Trainee;
import com.epam.gym.entity.Trainer;
import com.epam.gym.storage.TraineeStorage;
import com.epam.gym.storage.TrainerStorage;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.Collection;

@Repository
@Getter
// TODO:
//  @Getter...hmmm. Are you sure that you want other classes to access the storage? It breaks encapsulation.
//  You should provide methods to work with the storage instead of exposing it to other classes.
public class TraineeDao {


    private TraineeStorage storage;

    @Autowired
    public void setStorage(TraineeStorage storage) {
        this.storage = storage;
    }

    public void save(Trainee trainee) {
        storage.getTraineeMap().put(trainee.getId(), trainee);
    }

    public Trainee findById(String id) {
        return storage.getTraineeMap().get(id);
    }

    public Collection<Trainee> findAll() {
        return storage.getTraineeMap().values();
    }
}
