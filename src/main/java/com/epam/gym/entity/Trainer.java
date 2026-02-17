package com.epam.gym.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class Trainer extends User  {
    private String specialization;
}
