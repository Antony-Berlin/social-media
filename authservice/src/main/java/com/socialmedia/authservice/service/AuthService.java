package com.socialmedia.authservice.service;

import com.socialmedia.authservice.model.UserCredential;
import com.socialmedia.authservice.repository.UserCredentialRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserCredentialRepository repository;
    private final JwtService jwtService;
    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    public String register(UserCredential user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        repository.save(user);
        return "User registered successfully";
    }

    public String login(String username, String password) {
        var user = repository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));
        if (passwordEncoder.matches(password, user.getPassword())) {
            return jwtService.generateToken(username);
        }
        throw new RuntimeException("Invalid credentials");
    }

    public boolean validateToken(String token) {
        String username = jwtService.extractUsername(token);
        return jwtService.validateToken(token, username);
    }
}
