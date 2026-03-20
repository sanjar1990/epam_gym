package com.epam.gym.controller;

import com.epam.gym.dto.AuthDTO;
import com.epam.gym.dto.UserChangePasswordRequestDTO;
import com.epam.gym.service.AuthService;
import com.epam.gym.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/api/v1/auth")
@Tag(name = "Auth Api list", description = "this api is for Authentication")
// TODO:
//  With a single constructor, @Autowired (and onConstructor) is unnecessary since Spring 4.3.
@RequiredArgsConstructor()
public class AuthController {

    private final AuthService authService;
    private final UserService userService;

    // TODO:
    //  [Optional]
    //  1. Be careful with introducing conventions like "/public" in URLs — they often don’t scale well.
    //  For example, would we then introduce "/private/login" or "/private/logout" later?
    //  Typically, access control is better handled via security configuration rather than encoded in the path
    //  2. Consider whether we really need to introduce a custom ApiResponse<T> wrapper. ResponseEntity already covers
    //  HTTP-level metadata, so this may add extra nesting and duplicate status/error information without much benefit.
    //  3. Also please recheck your comments, they can be misleading if different from the actual source code

    //    3. Login (GET method)
    @PostMapping("/login")
    @Operation(summary = "User login", description = "")
    @ResponseStatus(HttpStatus.OK)
    public void login(@Valid @RequestBody AuthDTO dto) {
        log.info("Login auth: {} ", dto.getUsername());
        authService.login(dto);
    }

    //    4. Change Password (PUT method)
    @PostMapping("/change-password")
    @Operation(summary = "User Change Password", description = "")
    @ResponseStatus(HttpStatus.OK)
    public void changePassword(@Valid @RequestBody UserChangePasswordRequestDTO dto) {
        System.out.println("Login auth: " + dto.getNewPassword());
        userService.changePassword(dto);
    }

}
