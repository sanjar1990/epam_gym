package com.epam.gym.util;

import com.epam.gym.entity.User;
import org.springframework.stereotype.Component;
import java.util.Collection;
import java.util.Random;

@Component
// TODO:
//  Single Responsibility Principle violation.
//  A canonical definition of SRP - class should have only one reason to change.
//  In addition, username & password generation methods do not share common logic.
//  So it is better to have a separate class for each of them. Example: UsernameGenerator and PasswordGenerator
//  In general, do not hesitate to use many small focused classes - will be much easier to maintain when project grows.

public class UsernamePasswordGenerator {



}
