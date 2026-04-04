package com.epam.gym.service;

import com.epam.gym.entity.UserRole;
import com.epam.gym.enums.UserRoleEnum;
import com.epam.gym.repository.UserRoleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.LinkedList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserRoleService {
    private final UserRoleRepository userRoleRepository;
    public void merge(Long userId, List<UserRoleEnum> roles) {
            roles.forEach(role -> create(userId, role));
    }


    public void create(Long userId, UserRoleEnum role) {
        UserRole userRoleEntity = new UserRole();
        userRoleEntity.setRole(role);
        userRoleEntity.setUserId(userId);
        userRoleRepository.save(userRoleEntity);
    }
    public List<UserRoleEnum> getByProfileId(Long id) {
        List<UserRole> allEntity = userRoleRepository.findByUserId(id);
        List<UserRoleEnum> roles = new LinkedList<>();
        allEntity.forEach(entity -> {
            roles.add(entity.getRole());
        });
        return roles;
    }

}
