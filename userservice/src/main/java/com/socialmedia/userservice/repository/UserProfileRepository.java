package com.socialmedia.userservice.repository;

import com.socialmedia.userservice.model.UserProfile;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserProfileRepository extends JpaRepository<UserProfile, String> {
}
