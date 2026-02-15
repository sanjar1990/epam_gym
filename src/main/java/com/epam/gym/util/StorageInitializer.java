package com.epam.gym.util;

import com.epam.gym.entity.Trainee;
import com.epam.gym.entity.Trainer;
import com.epam.gym.entity.Training;
import com.epam.gym.entity.TrainingType;
import com.epam.gym.storage.TraineeStorage;
import com.epam.gym.storage.TrainerStorage;
import com.epam.gym.storage.TrainingStorage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Slf4j
@Component
// TODO:
//  First of all - good job, nice approach on using BeanPostProcessor to load data into storages!
//  [Optional]
//  It is very useful to have more descriptive full class name in this case, like StorageInitializationPostProcessor
//  or StorageInitializationBeanPostProcessor. Otherwise the critical lifecycle role of the class is hidden,
//  especially when it is under 'utils' package. Please also move it to 'config'
public class StorageInitializer implements BeanPostProcessor {
    @Value("${storage.init.trainees.file}")
    private String traineesFilePath;
    @Value("${storage.init.trainers.file}")
    private String trainersFilePath;
    @Value("${storage.init.trainings.file}")
    private String trainingsFilePath;

    @Override
    public Object postProcessAfterInitialization(Object bean, String name)
            throws BeansException {

        // TODO:
        //  1. Consider using pattern variable `if (bean instanceof TrainerStorage storage) {...}` to avoid unnecessary casts
        //  2. Use 'else if' since you need only one of these conditions to be true for each Storage type
        if (bean instanceof TrainerStorage) {

            TrainerStorage storage = (TrainerStorage) bean;
            loadTrainerData(storage);
        }

        if (bean instanceof TraineeStorage) {
            TraineeStorage storage = (TraineeStorage) bean;
            loadTraineeData(storage);
        }

        if (bean instanceof TrainingStorage) {
            TrainingStorage storage = (TrainingStorage) bean;
            loadTrainingData(storage);
        }


        return bean;
    }

    // TODO:
    //  [Optional]
    //  Consider switching to for-each loop here since you don't need the index
    //  Also you should be warned about this and other things by your IDE
    //  It's a good practice to pay attention to such warnings as they can help you write cleaner and more efficient code
    //  You can run `Inspect Code` in IntelliJ IDEA to find all warnings in the project
    private void loadTrainerData(TrainerStorage storage) {
        String[] data = ReadFile.getData(trainersFilePath);
        for (int i = 0; i < data.length; i++) {
            String[] parts = data[i].split(",");

            Trainer trainer = new Trainer();
            trainer.setId(parts[0]);
            trainer.setFirstName(parts[1]);
            trainer.setLastName(parts[2]);
            trainer.setUsername(parts[3]);
            trainer.setPassword(parts[4]);
            trainer.setActive(Boolean.parseBoolean(parts[5]));
            trainer.setSpecialization(parts[6]);
            storage.getTrainerMap().put(trainer.getId(), trainer);

        }
        log.info("Loaded {} trainers", storage.getTrainerMap().size());
    }

    private void loadTraineeData(TraineeStorage storage) {
        String[] data = ReadFile.getData(traineesFilePath);
        for (int i = 0; i < data.length; i++) {

            String[] parts = data[i].split(",");

            Trainee trainee = new Trainee();
            trainee.setId(parts[0]);
            trainee.setFirstName(parts[1]);
            trainee.setLastName(parts[2]);
            trainee.setUsername(parts[3]);
            trainee.setPassword(parts[4]);
            trainee.setActive(Boolean.parseBoolean(parts[5]));
            trainee.setDateOfBirth(LocalDate.parse(parts[6]));
            trainee.setAddress(parts[7]);

            storage.getTraineeMap().put(trainee.getId(), trainee);
        }
        log.info("Loaded {} trainees", storage.getTraineeMap().size());
    }

    private void loadTrainingData(TrainingStorage storage) {
        String[] data = ReadFile.getData(trainingsFilePath);
        for (int i = 0; i < data.length; i++) {
            String[] parts = data[i].split(",");

            Training training = new Training();
            training.setId(parts[0]);
            training.setTrainerId(parts[1]);
            training.setTraineeId(parts[2]);
            training.setTrainingName(parts[3]);
            training.setTrainingType(new TrainingType(parts[4]));
            training.setTrainingDate(LocalDate.parse(parts[5]));
            training.setDuration(Double.parseDouble(parts[6]));

            storage.getTrainningMap().put(training.getId(), training);
        }
        log.info("Loaded {} trainings", storage.getTrainningMap().size());
    }


}
