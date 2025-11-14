package com.socialmedia.feedservice.controller;

import com.socialmedia.feedservice.dto.FeedItem;
import com.socialmedia.feedservice.dto.FeedPage;
import com.socialmedia.feedservice.service.FeedService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/feed")
@RequiredArgsConstructor
public class FeedController {

    private final FeedService feedService;

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

    @GetMapping
    public ResponseEntity<FeedPage> getFeed(
            @RequestHeader(value = "Authorization", required = false) String authHeader,
            @RequestParam(value = "token", required = false) String tokenParam,
            @RequestParam(defaultValue = "10") Integer limit,
            @RequestParam(required = false) Long cursor) {

        String token = extractToken(authHeader, tokenParam);
        
        return ResponseEntity.ok(feedService.getUserFeed(token, limit, cursor));
    }

}
