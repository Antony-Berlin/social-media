package com.socialmedia.feedservice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class FeedPage {
    private List<FeedItem> items;
    private Long nextCursor;
    private boolean hasMore;
}
