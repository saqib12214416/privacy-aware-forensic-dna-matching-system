package com.forensicdna.controller;

import com.forensicdna.dto.LoginRequest;
import com.forensicdna.dto.LoginResponse;
import com.forensicdna.service.AuthService;

import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@CrossOrigin(origins = "http://localhost:5173")
public class AuthController {


    private final AuthService authService;


    public AuthController(AuthService authService){
        this.authService = authService;
    }


    @PostMapping("/login")
    public LoginResponse login(
            @RequestBody LoginRequest request
    ){

        return authService.login(request);

    }

}