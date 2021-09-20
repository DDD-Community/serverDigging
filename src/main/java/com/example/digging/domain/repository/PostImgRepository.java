package com.example.digging.domain.repository;

import com.example.digging.domain.entity.PostImg;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PostImgRepository extends JpaRepository<PostImg, Integer> {

    PostImg findByPostsPostId(Integer postid);
}
