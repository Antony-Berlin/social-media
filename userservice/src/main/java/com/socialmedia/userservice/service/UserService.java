package com.socialmedia.userservice.service;

import com.socialmedia.userservice.exception.*;
import com.socialmedia.userservice.model.UserProfile;
import com.socialmedia.userservice.repository.UserProfileRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import com.fasterxml.jackson.databind.*;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserProfileRepository repository;
    private final RestTemplate restTemplate = new RestTemplate();

    @Value("${auth.service.url}")
    private String authServiceUrl; // e.g. http://auth-service:8081/auth/validate?token=

    // Validate token and extract userId
    private String validateTokenAndGetUserId(String token) {
        Boolean valid = false;
        try {
            // 1. Get the raw JSON response as a String
            String jsonString = restTemplate.getForObject(authServiceUrl + token, String.class);

            // Check if the string is null or empty before parsing
            if (jsonString != null && !jsonString.isEmpty()) {
                // 2. Use the ObjectMapper to read the String into a JsonNode
                ObjectMapper mapper = new ObjectMapper();
                JsonNode rootNode = mapper.readTree(jsonString);

                // 3. Access the validation field
                // Note: rootNode.get("validation") returns a JsonNode, so call .asBoolean()
                valid = rootNode.get("validation").asBoolean();
            }
        } catch (Exception e) {
            throw new UnauthorizedException("Token validation failed" + e);
        }
        if (Boolean.TRUE.equals(valid)) {
            // Decode JWT manually to extract "sub" as userId
            try {
                String[] parts = token.split("\\.");
                if (parts.length != 3)
                    throw new UnauthorizedException("Invalid token format");
                String payload = new String(java.util.Base64.getUrlDecoder().decode(parts[1]));
                // payload is JSON, extract sub
                ObjectMapper mapper = new ObjectMapper();
                var node = mapper.readTree(payload);
                return node.get("sub").asText();
            } catch (Exception e) {
                throw new UnauthorizedException("Unable to extract userId from token");
            }
        } else {
            throw new UnauthorizedException("Token invalid or expired");
        }
    }

    public UserProfile createOrUpdateUser(UserProfile user, String token) {

        System.out.println("Creating/Updating user with ID: " + token);
        String userId = validateTokenAndGetUserId(token);
        user.setId(userId);
        return repository.save(user);
    }

    public UserProfile getUser(String token) {
        String userId = validateTokenAndGetUserId(token);
        return repository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User profile not found"));
    }

    public List<UserProfile> getAllUsers(String token) {
        validateTokenAndGetUserId(token);
        return repository.findAll();
    }

    public void deleteUser(String token) {
        String userId = validateTokenAndGetUserId(token);
        if (!repository.existsById(userId))
            throw new UserNotFoundException("User profile not found");
        repository.deleteById(userId);
    }
}
