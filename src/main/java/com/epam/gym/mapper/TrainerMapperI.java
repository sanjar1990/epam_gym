package com.epam.gym.mapper;

public interface TrainerMapperI {
    Long getTrainerId();

    //User
    String getFirstName();

    String getLastName();

    String getTrainerUsername();

    Boolean getIsActive();

    //Training Type
    Long getTrainingTypeId();

    String getTrainingTypeName();

}
