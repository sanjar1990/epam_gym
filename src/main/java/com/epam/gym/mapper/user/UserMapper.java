package com.epam.gym.mapper.user;

import com.epam.gym.dto.UserDTO;
import com.epam.gym.entity.User;

public class UserMapper {
    public static UserDTO toUserDTO(User user) {
        UserDTO userDTO = new UserDTO();
        userDTO.setFirstName(user.getFirstName());
        userDTO.setLastName(user.getLastName());
        userDTO.setUsername(user.getUsername());
        userDTO.setIsActive(user.getIsActive());
        return userDTO;
    }
}
