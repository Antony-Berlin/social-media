package com.socialmedia.authservice.controller;

import com.socialmedia.authservice.model.UserCredential;
import com.socialmedia.authservice.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.Map;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService service;

    @PostMapping("/register")
    public ResponseEntity<Map<String, Object>> register(@RequestBody UserCredential user) {
        return ResponseEntity.ok(service.register(user));
    }

    @PostMapping("/login")
    public ResponseEntity<Map<String, Object>> login(@RequestBody UserCredential user) {
        return ResponseEntity.ok(service.login(user.getUsername(), user.getPassword()));
    }

    @GetMapping("/validate")
    public ResponseEntity<Map<String, Object>> validate(@RequestParam String token) {
        return ResponseEntity.ok(service.validateToken(token));
    }
}
