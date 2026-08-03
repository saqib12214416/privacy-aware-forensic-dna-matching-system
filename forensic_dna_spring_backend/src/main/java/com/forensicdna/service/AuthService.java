package com.forensicdna.service;

import com.forensicdna.config.JwtUtil;
import com.forensicdna.dto.LoginRequest;
import com.forensicdna.dto.LoginResponse;
import com.forensicdna.entity.User;
import com.forensicdna.repository.UserRepository;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    private final UserRepository userRepository;
    private final JwtUtil jwtUtil;

    public AuthService(UserRepository userRepository, JwtUtil jwtUtil) {
        this.userRepository = userRepository;
        this.jwtUtil = jwtUtil;
    }

    public LoginResponse login(LoginRequest request) {

        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new RuntimeException("Invalid email or password"));

        // Temporary simple password check because your DB currently stores password_hash = 'admin'
        if (!user.getPasswordHash().equals(request.getPassword())) {
            throw new RuntimeException("Invalid email or password");
        }

        String roleCode = user.getRole().getCode();

        String token = jwtUtil.generateToken(user.getEmail(), roleCode);

        return new LoginResponse(token, user.getEmail(), roleCode);
    }
}