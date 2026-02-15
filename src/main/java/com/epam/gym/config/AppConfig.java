package com.epam.gym.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
// TODO:
//  @SpringBootApplication is already annotated with @ComponentScan
//  click on the annotation at the GymApplication.java to see what else is included
//  Similarly @PropertySource, if file location and name are default boot will automatically load it.
@ComponentScan("com.epam.gym")
@PropertySource("classpath:application.properties")
public class AppConfig {

}
