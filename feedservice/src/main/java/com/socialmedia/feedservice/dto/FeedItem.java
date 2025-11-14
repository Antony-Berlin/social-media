package com.socialmedia.feedservice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class FeedItem {
    private Integer postId;
    private String authorId;
    private String content;
    private Long timestamp;
}
