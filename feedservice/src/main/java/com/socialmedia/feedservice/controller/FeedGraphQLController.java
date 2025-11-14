package com.socialmedia.feedservice.controller;

import com.socialmedia.feedservice.dto.FeedItem;
import com.socialmedia.feedservice.dto.FeedPage;
import com.socialmedia.feedservice.service.FeedService;
import lombok.RequiredArgsConstructor;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class FeedGraphQLController {

    private final FeedService feedService;

    @QueryMapping
    public FeedPage feed(
            @Argument String token,
            @Argument Integer limit,
            @Argument Long cursor) {

            
        return feedService.getUserFeed(token, limit, cursor);
    }

}
