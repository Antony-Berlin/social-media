package com.socialmedia.postservice.repository;

import com.socialmedia.postservice.model.Post;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PostRepository extends JpaRepository<Post, Long> {
    List<Post> findByAuthorId(String authorId);
}
