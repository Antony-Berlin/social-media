package com.socialmedia.postservice.controller;

import com.socialmedia.postservice.model.Post;
import com.socialmedia.postservice.service.PostService;
import com.socialmedia.postservice.util.JwtValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/posts")
@RequiredArgsConstructor
public class PostController {

    @Autowired
    private JwtValidator jwtValidator;

    private final PostService postService;

    private String extractUserId(String token) {
        return jwtValidator.validateTokenAndGetUserId(token);
    }


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

    @PostMapping
    public ResponseEntity<Post> createPost(@RequestHeader(value = "Authorization", required = false) String authHeader,
            @RequestParam(value = "token", required = false) String tokenParam,
            @RequestBody Post post) {
                String token = extractToken(authHeader, tokenParam);
        String userId = extractUserId(token);
        return ResponseEntity.ok(postService.createPost(post, userId));
    }

    @GetMapping
    public ResponseEntity<List<Post>> getAllPosts() {
        return ResponseEntity.ok(postService.getAllPosts());
    }

    @GetMapping("/me")
    public ResponseEntity<List<Post>> getMyPosts(@RequestHeader(value = "Authorization", required = false) String authHeader,
            @RequestParam(value = "token", required = false) String tokenParam) {
        String token = extractToken(authHeader, tokenParam);
        String userId = extractUserId(token);
        return ResponseEntity.ok(postService.getPostsByUser(userId));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Post> getPostById(@PathVariable Long id,
            @RequestHeader(value = "Authorization", required = false) String authHeader,
            @RequestParam(value = "token", required = false) String tokenParam) {
        String token = extractToken(authHeader, tokenParam);
        String userId = extractUserId(token);
        return ResponseEntity.ok(postService.getPostById(id));
    }   

    @GetMapping("user/{id}")
    public ResponseEntity<List<Post>> getPostById(@PathVariable String id,
            @RequestHeader(value = "Authorization", required = false) String authHeader,
            @RequestParam(value = "token", required = false) String tokenParam) {
        String token = extractToken(authHeader, tokenParam);
        String userId = extractUserId(token);
        return ResponseEntity.ok(postService.getPostsByUser(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Post> updatePost(@PathVariable Long id,
            @RequestHeader(value = "Authorization", required = false) String authHeader,
            @RequestParam(value = "token", required = false) String tokenParam,
            @RequestBody Post post) {
        String token = extractToken(authHeader, tokenParam);
        String userId = extractUserId(token);
        return ResponseEntity.ok(postService.updatePost(id, post, userId));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deletePost(@PathVariable Long id,
            @RequestHeader(value = "Authorization", required = false) String authHeader,
            @RequestParam(value = "token", required = false) String tokenParam) {
        String token = extractToken(authHeader, tokenParam);
        String userId = extractUserId(token);
        postService.deletePost(id, userId);
        return ResponseEntity.ok("Post deleted successfully");
    }
}
