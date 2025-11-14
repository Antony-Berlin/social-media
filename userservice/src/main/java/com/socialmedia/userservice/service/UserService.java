package com.socialmedia.userservice.service;

import com.socialmedia.userservice.exception.*;
import com.socialmedia.userservice.model.UserProfile;
import com.socialmedia.userservice.repository.UserProfileRepository;
import lombok.RequiredArgsConstructor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.socialmedia.userservice.utils.JwtValidator;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {


    @Autowired
    private JwtValidator jwtValidator;
    
    private final UserProfileRepository repository;



    public UserProfile createOrUpdateUser(UserProfile user, String token) {

        System.out.println("Creating/Updating user with ID: " + token);
        String userId = jwtValidator.validateTokenAndGetUserId(token);
        user.setId(userId);
        return repository.save(user);
    }

    public UserProfile getUser(String token) {
        String userId = jwtValidator.validateTokenAndGetUserId(token);
        return repository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User profile not found"));
    }

    public List<UserProfile> getAllUsers(String token) {
        jwtValidator.validateTokenAndGetUserId(token);
        return repository.findAll();
    }

    public void deleteUser(String token) {
        String userId = jwtValidator.validateTokenAndGetUserId(token);
        if (!repository.existsById(userId))
            throw new UserNotFoundException("User profile not found");
        repository.deleteById(userId);
    }
}
