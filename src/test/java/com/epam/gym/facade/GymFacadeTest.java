package com.epam.gym.facade;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertNotNull;

// TODO:
//  Even though this test class is kinda empty now, later I guess here will be more tests which are end-2-end like
//  Plus you are using Spring boot, so @SpringBootTest could be better option here.
//  (It will load the whole context which will be most likely needed for the e2e testing)
@SpringBootTest
class GymFacadeTest {

    @Autowired
    private GymFacade facade;

    @Test
    void shouldAccessFacade() {
        assertNotNull(facade);
    }
}