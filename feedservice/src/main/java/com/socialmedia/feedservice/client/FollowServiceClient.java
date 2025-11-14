package com.socialmedia.feedservice.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;

import java.util.List;

@FeignClient(name = "follow-service", url = "${services.follow}")
public interface FollowServiceClient {

    @GetMapping("/follow/following")
    List<String> getFollowing(@RequestHeader("Authorization") String authorization);
}
