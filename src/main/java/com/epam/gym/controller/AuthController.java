package com.epam.gym.controller;

import com.epam.gym.dto.ApiResponse;
import com.epam.gym.dto.AuthDTO;
import com.epam.gym.dto.UserChangePasswordRequestDTO;
import com.epam.gym.service.AuthService;
import com.epam.gym.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/api/v1/auth")
@Tag(name = "Auth Api list", description = "this api is for Authentication")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class AuthController {

    private final AuthService authService;
    private final UserService userService;

    //    3. Login (GET method)
    @PostMapping("/public/login")
    @Operation(summary = "User login", description = "")
    public ResponseEntity<ApiResponse<?>> login(@Valid @RequestBody AuthDTO dto) {
        log.info("Login auth: {} ", dto.getUsername());
        return ResponseEntity.ok(authService.login(dto));
    }

    //    4. Change Login (PUT method)
    @PostMapping("/public/change-password")
    @Operation(summary = "User Change Password", description = "")
    public ResponseEntity<ApiResponse<?>> changePassword(@Valid @RequestBody UserChangePasswordRequestDTO dto) {
        System.out.println("Login auth: " + dto.getNewPassword());
        return ResponseEntity.ok(userService.changePassword(dto));
    }

}
