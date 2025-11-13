package com.socialmedia.authservice.service;

import com.socialmedia.authservice.exception.*;
import com.socialmedia.authservice.model.UserCredential;
import com.socialmedia.authservice.repository.UserCredentialRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserCredentialRepository repository;
    private final JwtService jwtService;
    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    public Map<String, Object> register(UserCredential user) {
        if (repository.findByUsername(user.getUsername()).isPresent()) {
            throw new UserAlreadyExistsException("Username already taken");
        }
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        repository.save(user);
        return Map.of("message", "User registered successfully");
    }

    public Map<String, Object> login(String username, String password) {
        var user = repository.findByUsername(username)
                .orElseThrow(() -> new UserNotFoundException("User not found"));

        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new InvalidCredentialsException("Wrong password");
        }

        return Map.of("token", jwtService.generateToken(username));
    }

    public Map<String, Object> validateToken(String token) {
        try {
            String username = jwtService.extractUsername(token);
            return Map.of("validation" , jwtService.validateToken(token, username));
        } catch (Exception e) {
            throw new InvalidCredentialsException("Invalid or expired token");
        }
    }
}
