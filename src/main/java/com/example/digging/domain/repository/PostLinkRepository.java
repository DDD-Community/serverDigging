package com.example.digging.domain.repository;

import com.example.digging.domain.entity.PostLink;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PostLinkRepository extends JpaRepository<PostLink, Integer> {
}
