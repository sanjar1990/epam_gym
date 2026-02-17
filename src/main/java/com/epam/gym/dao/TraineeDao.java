package com.epam.gym.dao;

import com.epam.gym.entity.Trainee;
import com.epam.gym.entity.User;
import com.epam.gym.exceptions.UsernameExistsException;
import com.epam.gym.storage.TraineeStorage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.Collection;

@Repository
// TODO
//  @Getter...hmmm. Are you sure that you want other classes to access the storage? It breaks encapsulation.
//  You should provide methods to work with the storage instead of exposing it to other classes.
// Done
public class TraineeDao {


    private TraineeStorage storage;


    @Autowired
    public void setStorage(TraineeStorage storage) {
        this.storage = storage;
    }
    public void save(Trainee trainee) {
        storage.getTraineeMap().put(trainee.getId(), trainee);
    }
    public void update(Trainee trainee,Collection<? extends User> existingUsers) {

        boolean exists = existingUsers.stream()
                .anyMatch(u -> u.getUsername().startsWith(trainee.getUsername()));
        if (exists) {
            throw new UsernameExistsException("Trainee with username " + trainee.getUsername() + " already exists");
        }
        storage.getTraineeMap().put(trainee.getId(), trainee);
    }

    public Trainee findById(Long id) {
        return storage.getTraineeMap().get(id);
    }

    public Collection<Trainee> findAll() {
        return storage.getTraineeMap().values();
    }

    public void remove(Long id) {
        storage.getTraineeMap().remove(id);
    }



}
