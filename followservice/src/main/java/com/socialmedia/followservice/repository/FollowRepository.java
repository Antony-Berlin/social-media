package com.socialmedia.followservice.repository;

import com.socialmedia.followservice.model.Follow;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface FollowRepository extends JpaRepository<Follow, String> {

    List<Follow> findByFollowerId(String followerId);

    List<Follow> findByFollowingId(String followingId);

    Optional<Follow> findByFollowerIdAndFollowingId(String followerId, String followingId);

    boolean existsByFollowerIdAndFollowingId(String followerId, String followingId);

    void deleteByFollowerIdAndFollowingId(String followerId, String followingId);
}
