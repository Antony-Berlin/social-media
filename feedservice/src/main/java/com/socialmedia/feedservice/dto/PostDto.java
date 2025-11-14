package com.socialmedia.feedservice.dto;

import lombok.Data;

@Data
public class PostDto {
    private Integer id;
    private String text;
    private String authorId;
    private String createdAt; // Keep as String, convert later
}
