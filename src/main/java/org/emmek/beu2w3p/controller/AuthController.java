package org.emmek.beu2w3p.controller;

import org.emmek.beu2w3p.entities.User;
import org.emmek.beu2w3p.exceptions.BadRequestException;
import org.emmek.beu2w3p.payloads.UserLoginDTO;
import org.emmek.beu2w3p.payloads.UserLoginSuccessDTO;
import org.emmek.beu2w3p.payloads.UserPostDTO;
import org.emmek.beu2w3p.services.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequestMapping("/auth")
public class AuthController {
    @Autowired
    private AuthService authService;

    @PostMapping("/login")
    public UserLoginSuccessDTO login(@RequestBody @Validated UserLoginDTO body, BindingResult validation) {
        if (validation.hasErrors()) {
            throw new BadRequestException(validation.getAllErrors());
        } else {
            return new UserLoginSuccessDTO(authService.authenticateUser(body));
        }
    }

    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED) // <-- 201
    public User saveUser(@RequestBody @Validated UserPostDTO body, BindingResult validation) {
        if (validation.hasErrors()) {
            throw new BadRequestException(validation.getAllErrors());
        } else {
            try {
                return authService.registerUser(body);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }
}