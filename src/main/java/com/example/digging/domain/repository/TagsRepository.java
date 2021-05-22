package com.example.digging.domain.repository;

import com.example.digging.domain.entity.Tags;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TagsRepository extends JpaRepository<Tags, Integer> {

    Tags findByTagsAndUserId(String checkTag, Integer userId);

    Optional<Tags> findAllByUserId(Integer id);
}
