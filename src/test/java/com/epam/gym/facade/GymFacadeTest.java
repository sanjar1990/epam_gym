package com.epam.gym.facade;

import com.epam.gym.config.AppConfig;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

import static org.junit.jupiter.api.Assertions.*;

@SpringJUnitConfig(AppConfig.class)
class GymFacadeTest {

    @Autowired
    private GymFacade facade;

    @Test
    void shouldAccessFacade() {
        assertNotNull(facade);
    }
}