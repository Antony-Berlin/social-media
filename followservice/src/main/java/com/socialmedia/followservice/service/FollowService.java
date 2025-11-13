package com.socialmedia.followservice.service;

import com.socialmedia.followservice.model.Follow;
import com.socialmedia.followservice.repository.FollowRepository;
import lombok.RequiredArgsConstructor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import com.socialmedia.followservice.utils.JwtValidator;

@Service
@RequiredArgsConstructor
public class FollowService {

    @Autowired
    private JwtValidator jwtValidator;
    
    private final FollowRepository repository;


    public String followUser(String token, String targetUserId) {
        String followerId = jwtValidator.validateTokenAndGetUserId(token);
        if (followerId.equals(targetUserId))
            throw new IllegalArgumentException("Cannot follow yourself");

        if (repository.existsByFollowerIdAndFollowingId(followerId, targetUserId))
            return "Already following";

        Follow follow = Follow.builder()
                .followerId(followerId)
                .followingId(targetUserId)
                .build();
        repository.save(follow);
        return "Followed successfully";
    }

    @Transactional
    public String unfollowUser(String token, String targetUserId) {
        String followerId = jwtValidator.validateTokenAndGetUserId(token);
        repository.deleteByFollowerIdAndFollowingId(followerId, targetUserId);
        return "Unfollowed successfully";
    }

    public List<String> getFollowers(String targetUserId, String token) {
        jwtValidator.validateTokenAndGetUserId(token);
        return repository.findByFollowingId(targetUserId)
                .stream().map(Follow::getFollowerId).toList();
    }

    public List<String> getFollowing(String token) {
        String userId = jwtValidator.validateTokenAndGetUserId(token);
        return repository.findByFollowerId(userId)
                .stream().map(Follow::getFollowingId).toList();
    }

    public boolean isFollowing(String token, String targetUserId) {
        String userId = jwtValidator.validateTokenAndGetUserId(token);
        return repository.existsByFollowerIdAndFollowingId(userId, targetUserId);
    }
}
