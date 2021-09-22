package com.example.digging.domain.repository;

import com.example.digging.domain.entity.Tags;
import com.example.digging.domain.entity.User;
import com.google.common.io.Files;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.security.AlgorithmParameterGenerator;
import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {
    @EntityGraph(attributePaths = "authorities")
    Optional<User> findOneWithAuthoritiesByUid(String username);

    List<User> findByUsernameStartsWith(String username);


    Optional<User> findByUid(String uid);
    boolean existsByUid(String uid);

}
