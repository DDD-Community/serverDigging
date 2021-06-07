package com.example.digging.domain.repository;

import com.example.digging.domain.entity.PostTag;
import com.example.digging.domain.entity.Tags;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PostTagRepository extends JpaRepository<PostTag, Integer> {

    List<PostTag> findAllByPostsPostId(Integer postid);

    List<PostTag> findAllByTagsTagId(Integer tagId);
}
