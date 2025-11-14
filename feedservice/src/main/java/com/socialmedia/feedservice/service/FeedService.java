package com.socialmedia.feedservice.service;

import com.socialmedia.feedservice.client.FollowServiceClient;
import com.socialmedia.feedservice.client.PostServiceClient;
import com.socialmedia.feedservice.dto.FeedItem;
import com.socialmedia.feedservice.dto.FeedPage;
import com.socialmedia.feedservice.dto.PostDto;
import com.socialmedia.feedservice.util.JwtValidator;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class FeedService {

    private final FollowServiceClient followClient;
    private final PostServiceClient postClient;
    private final JwtValidator jwtValidator;

    @CircuitBreaker(name = "followService", fallbackMethod = "fallbackFollowing")
    public List<String> getFollowing(String token) {
        return followClient.getFollowing("Bearer " + token);
    }

    @CircuitBreaker(name = "postService", fallbackMethod = "fallbackPosts")
    public List<PostDto> getPosts(String userId, String token) {
        return postClient.getPosts(userId, "Bearer " + token);
    }

    public FeedPage getUserFeed(String token, Integer limit, Long cursor) {
        String userId = jwtValidator.validateTokenAndGetUserId(token);

        List<String> following = getFollowing(token);

        List<PostDto> posts = new ArrayList<>();
        for (String f : following) {
            posts.addAll(getPosts(f, token));
        }

        // Convert and sort DESCENDING
        List<FeedItem> feedItems = posts.stream()
                .map(p -> new FeedItem(
                        p.getId(),
                        p.getAuthorId(),
                        p.getText(),
                        parseTimestamp(p.getCreatedAt())
                ))
                .sorted((a, b) -> Long.compare(b.getTimestamp(), a.getTimestamp()))
                .toList();

        // Apply cursor: keep posts older than cursor
        if (cursor != null) {
            feedItems = feedItems.stream()
                    .filter(p -> p.getTimestamp() < cursor)
                    .toList();
        }

        // Take first N
        List<FeedItem> pageItems = feedItems.stream()
                .limit(limit)
                .toList();

        Long nextCursor = pageItems.isEmpty()
                ? null
                : pageItems.get(pageItems.size() - 1).getTimestamp();

        boolean hasMore = feedItems.size() > limit;

        return new FeedPage(pageItems, nextCursor, hasMore);
    }

    private Long parseTimestamp(String iso) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSSSS");
        LocalDateTime dateTime = LocalDateTime.parse(iso, formatter);
        return dateTime.atZone(java.time.ZoneId.systemDefault()).toInstant().toEpochMilli();
    }


    // ---- FALLBACKS ----
    public List<String> fallbackFollowing(String userId, Throwable ex) {
        return List.of(); // empty following list
    }

    public List<PostDto> fallbackPosts(String userId, Throwable ex) {
        return List.of(); // empty posts list
    }

}
