package com.example.digging.domain.repository;

import com.example.digging.domain.entity.PostText;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PostTextRepository extends JpaRepository<PostText, Integer> {
    PostText findByPostsPostId(Integer postid);
}
