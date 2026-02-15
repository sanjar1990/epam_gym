package com.epam.gym.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
// TODO
//  Since it is a Spring boot application and you are using @SpringBootApplication in the main class,
//  you don't need to use @ComponentScan here because @SpringBootApplication is already annotated with it
//  click on the annotation at the GymApplication.java to see what is included
@ComponentScan("com.epam.gym")
@PropertySource("classpath:application.properties")
public class AppConfig{

}
