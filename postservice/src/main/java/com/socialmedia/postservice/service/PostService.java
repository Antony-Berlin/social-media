package com.socialmedia.postservice.service;

import com.socialmedia.postservice.exception.PostNotFoundException;
import com.socialmedia.postservice.model.Post;
import com.socialmedia.postservice.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;

    public Post createPost(Post post, String authorId) {
        post.setAuthorId(authorId);
        post.setCreatedAt(LocalDateTime.now());
        return postRepository.save(post);
    }

    public List<Post> getAllPosts() {
        return postRepository.findAll();
    }

    public List<Post> getPostsByUser(String authorId) {
        return postRepository.findByAuthorId(authorId);
    }

    public Post getPostById(Long id) {
        return postRepository.findById(id)
                .orElseThrow(() -> new PostNotFoundException("Post not found with id: " + id));
    }

    public Post updatePost(Long id, Post updatedPost, String authorId) {
        Post post = getPostById(id);
        if (!post.getAuthorId().equals(authorId)) {
            throw new RuntimeException("Unauthorized to update this post");
        }
        post.setText(updatedPost.getText());
        return postRepository.save(post);
    }

    public void deletePost(Long id, String authorId) {
        Post post = getPostById(id);
        if (!post.getAuthorId().equals(authorId)) {
            throw new RuntimeException("Unauthorized to delete this post");
        }
        postRepository.delete(post);
    }
}
