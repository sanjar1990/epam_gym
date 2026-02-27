package com.epam.gym;

import com.epam.gym.dto.CreateTrainerRequestDTO;
import com.epam.gym.service.TrainerService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class GymApplicationTests {
    @Autowired
    private TrainerService trainerService;

    @Test
    public void test() {

        CreateTrainerRequestDTO trainer = new CreateTrainerRequestDTO();
        trainer.setFirstName("Alex");
        trainer.setLastName("Smith");
        trainer.setTrainingTypeId(2L);
        trainerService.create(trainer);
    }

}
