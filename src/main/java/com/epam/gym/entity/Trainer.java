package com.epam.gym.entity;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Trainer extends User  {
    private String id;
    private String specialization;
}
