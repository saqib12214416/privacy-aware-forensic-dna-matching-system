package com.forensicdna.service;

import com.forensicdna.config.JwtUtil;
import com.forensicdna.entity.User;
import com.forensicdna.repository.UserRepository;

import org.springframework.stereotype.Service;

@Service
public class AuthService {

    private final UserRepository userRepository;
    private final JwtUtil jwtUtil;

    public AuthService(
            UserRepository userRepository,
            JwtUtil jwtUtil
    ) {
        this.userRepository = userRepository;
        this.jwtUtil = jwtUtil;
    }

    public String login(String email, String password) {

        User user = userRepository
                .findByEmail(email)
                .orElseThrow(() ->
                        new RuntimeException(
                                "Invalid email or password"
                        )
                );

        if (!Boolean.TRUE.equals(user.getIsActive())) {
            throw new RuntimeException("User account is inactive");
        }

        if (!password.equals(user.getPasswordHash())) {
            throw new RuntimeException(
                    "Invalid email or password"
            );
        }

        return jwtUtil.generateToken(user.getEmail());
    }
}