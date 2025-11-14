package com.socialmedia.feedservice.client;

import com.socialmedia.feedservice.dto.PostDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;

import java.util.List;

@FeignClient(name = "post-service", url = "${services.post}")
public interface PostServiceClient {

    @GetMapping("/posts/user/{userId}")
    List<PostDto> getPosts(@PathVariable String userId, @RequestHeader("Authorization") String authorization);
}
