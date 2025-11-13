package com.socialmedia.followservice.controller;

import com.socialmedia.followservice.service.FollowService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/follow")
@RequiredArgsConstructor
public class FollowController {

    private final FollowService followService;

    /**
     * Extract JWT token either from Authorization header or query param
     */
    private String extractToken(
            @RequestHeader(value = "Authorization", required = false) String authHeader,
            @RequestParam(value = "token", required = false) String tokenParam) {

        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            return authHeader.substring(7);
        } else if (tokenParam != null && !tokenParam.isEmpty()) {
            return tokenParam;
        } else {
            throw new IllegalArgumentException("Authorization token missing");
        }
    }

    @PostMapping("/{targetUserId}")
    public ResponseEntity<?> followUser(
            @PathVariable String targetUserId,
            @RequestHeader(value = "Authorization", required = false) String authHeader,
            @RequestParam(value = "token", required = false) String tokenParam) {
        try {
            String token = extractToken(authHeader, tokenParam);
            String response = followService.followUser(token, targetUserId);
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @DeleteMapping("/{targetUserId}")
    public ResponseEntity<?> unfollowUser(
            @PathVariable String targetUserId,
            @RequestHeader(value = "Authorization", required = false) String authHeader,
            @RequestParam(value = "token", required = false) String tokenParam) {
        try {
            String token = extractToken(authHeader, tokenParam);
            String response = followService.unfollowUser(token, targetUserId);
            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @GetMapping("/followers/{targetUserId}")
    public ResponseEntity<?> getFollowers(
            @PathVariable String targetUserId,
            @RequestHeader(value = "Authorization", required = false) String authHeader,
            @RequestParam(value = "token", required = false) String tokenParam) {
        try {
            String token = extractToken(authHeader, tokenParam);
            List<String> followers = followService.getFollowers(targetUserId, token);
            return ResponseEntity.ok(followers);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @GetMapping("/following")
    public ResponseEntity<?> getFollowing(
            @RequestHeader(value = "Authorization", required = false) String authHeader,
            @RequestParam(value = "token", required = false) String tokenParam) {
        try {
            String token = extractToken(authHeader, tokenParam);
            List<String> following = followService.getFollowing(token);
            return ResponseEntity.ok(following);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @GetMapping("/status/{targetUserId}")
    public ResponseEntity<?> isFollowing(
            @PathVariable String targetUserId,
            @RequestHeader(value = "Authorization", required = false) String authHeader,
            @RequestParam(value = "token", required = false) String tokenParam) {
        try {
            String token = extractToken(authHeader, tokenParam);
            boolean isFollowing = followService.isFollowing(token, targetUserId);
            return ResponseEntity.ok(isFollowing);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }
}
