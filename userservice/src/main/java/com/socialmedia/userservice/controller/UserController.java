package com.socialmedia.userservice.controller;

import com.socialmedia.userservice.model.UserProfile;
import com.socialmedia.userservice.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService service;

    private String extractToken(@RequestHeader(value = "Authorization", required = false) String authHeader,
            @RequestParam(value = "token", required = false) String tokenParam) {
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            return authHeader.substring(7);
        } else if (tokenParam != null) {
            return tokenParam;
        } else {
            throw new RuntimeException("Authorization token missing");
        }
    }

    @PostMapping("/me")
    public ResponseEntity<UserProfile> createOrUpdate(@RequestBody UserProfile user,
            @RequestHeader(value = "Authorization", required = false) String authHeader,
            @RequestParam(value = "token", required = false) String tokenParam) {
        String token = extractToken(authHeader, tokenParam);
        System.out.println("Received token: " + token);
        return ResponseEntity.ok(service.createOrUpdateUser(user, token));
    }

    @GetMapping("/me")
    public ResponseEntity<UserProfile> getMe(
            @RequestHeader(value = "Authorization", required = false) String authHeader,
            @RequestParam(value = "token", required = false) String tokenParam) {
        String token = extractToken(authHeader, tokenParam);
        return ResponseEntity.ok(service.getUser(token));
    }

    @GetMapping("/all")
    public ResponseEntity<List<UserProfile>> getAll(
            @RequestHeader(value = "Authorization", required = false) String authHeader,
            @RequestParam(value = "token", required = false) String tokenParam) {
        String token = extractToken(authHeader, tokenParam);
        return ResponseEntity.ok(service.getAllUsers(token));
    }

    @DeleteMapping("/me")
    public ResponseEntity<String> delete(@RequestHeader(value = "Authorization", required = false) String authHeader,
            @RequestParam(value = "token", required = false) String tokenParam) {
        String token = extractToken(authHeader, tokenParam);
        service.deleteUser(token);
        return ResponseEntity.ok("User deleted successfully");
    }
}
