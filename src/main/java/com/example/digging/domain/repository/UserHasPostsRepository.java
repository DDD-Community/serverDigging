package com.example.digging.domain.repository;

import com.example.digging.domain.entity.UserHasPosts;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserHasPostsRepository extends JpaRepository<UserHasPosts, Integer> {
    Optional<UserHasPosts> findByUserIdAndPostsPostId(Integer userId, Integer postId);
}
