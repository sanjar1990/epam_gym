package com.epam.gym.config;

import com.epam.gym.entity.Trainee;
import com.epam.gym.entity.Trainer;
import com.epam.gym.entity.Training;
import com.epam.gym.enums.TrainingType;
import com.epam.gym.storage.TraineeStorage;
import com.epam.gym.storage.TrainerStorage;
import com.epam.gym.storage.TrainingStorage;
import com.epam.gym.util.FileUtils;
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
public class StorageInitializationPostProcessor implements BeanPostProcessor {
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
        // DONE
        switch (bean) {
            case TrainerStorage storage -> loadTrainerData(storage);
            case TraineeStorage storage -> loadTraineeData(storage);
            case TrainingStorage storage -> loadTrainingData(storage);
            default -> {
            }
        }
        return bean;
    }

    // TODO:
    //  [Optional]
    //  Consider switching to for-each loop here since you don't need the index
    //  Also you should be warned about this and other things by your IDE
    //  It's a good practice to pay attention to such warnings as they can help you write cleaner and more efficient code
    //  You can run `Inspect Code` in IntelliJ IDEA to find all warnings in the project
    //Done
    private void loadTrainerData(TrainerStorage storage) {
        String[] data = FileUtils.readLines(trainersFilePath);
        for (String datum : data) {
            String[] parts = datum.split(",");

            Trainer trainer = new Trainer();
            trainer.setFirstName(parts[0]);
            trainer.setLastName(parts[1]);
            trainer.setUsername(parts[2]);
            trainer.setPassword(parts[3]);
            trainer.setActive(Boolean.parseBoolean(parts[4]));
            trainer.setSpecialization(parts[5]);
            storage.getTrainerMap().put(trainer.getId(), trainer);

        }
        log.info("Loaded {} trainers", storage.getTrainerMap().size());
    }

    private void loadTraineeData(TraineeStorage storage) {
        String[] data = FileUtils.readLines(traineesFilePath);
        for (String datum : data) {

            String[] parts = datum.split(",");

            Trainee trainee = new Trainee();
            trainee.setFirstName(parts[0]);
            trainee.setLastName(parts[1]);
            trainee.setUsername(parts[2]);
            trainee.setPassword(parts[3]);
            trainee.setActive(Boolean.parseBoolean(parts[4]));
            trainee.setDateOfBirth(LocalDate.parse(parts[5]));
            trainee.setAddress(parts[6]);

            storage.getTraineeMap().put(trainee.getId(), trainee);
        }
        log.info("Loaded {} trainees", storage.getTraineeMap().size());
    }

    private void loadTrainingData(TrainingStorage storage) {
        String[] data = FileUtils.readLines(trainingsFilePath);
        for (String datum : data) {
            String[] parts = datum.split(",");

            Training training = new Training();
            training.setTrainerId(parts[0]);
            training.setTraineeId(parts[1]);
            training.setTrainingName(parts[2]);
            training.setTrainingType(TrainingType.valueOf(parts[3]));
            training.setTrainingDate(LocalDate.parse(parts[4]));
            System.out.println("Integer::: "+parts[5]);
            training.setTrainingDuration(Integer.parseInt(parts[5].trim()));

            storage.getTrainningMap().put(training.getId(), training);
        }
        log.info("Loaded {} trainings", storage.getTrainningMap().size());
    }


}
