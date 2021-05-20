package com.example.digging.domain.repository;

import com.example.digging.domain.entity.Imgs;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ImgsRepository extends JpaRepository<Imgs, Integer> {
}
