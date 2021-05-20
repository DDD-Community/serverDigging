package com.example.digging.domain.repository;

import com.example.digging.domain.entity.UserHasPosts;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserHasPostsRepository extends JpaRepository<UserHasPosts, Integer> {
}
