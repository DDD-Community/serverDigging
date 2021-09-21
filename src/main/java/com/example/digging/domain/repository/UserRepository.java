package com.example.digging.domain.repository;

import com.example.digging.domain.entity.Tags;
import com.example.digging.domain.entity.User;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.security.AlgorithmParameterGenerator;
import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {
    @EntityGraph(attributePaths = "authorities")
    Optional<User> findOneWithAuthoritiesByUsername(String username);

    Optional<Object> findOneWithAuthoritiesByOauthId(String oauthId);

    List<User> findByUsernameStartsWith(String username);


    Optional<User> findByUsername(String username);

    User findByUsernameAndProvider(String username, String provider);
}
