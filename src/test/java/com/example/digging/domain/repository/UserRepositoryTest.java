package com.example.digging.domain.repository;

import com.example.digging.domain.entity.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDateTime;

public class UserRepositoryTest {

    // Dependency Injection
    @Autowired
    private UserRepository userRepository;

    @Test
    public void create(){
        User user = new User();
        user.setId(1);
        user.setCreatedAt(LocalDateTime.now());
        user.setEmail("digging@gmail.com");
        user.setPassword("password");
        user.setUsername("testuser1");
        user.setProvider("admin");
        user.setRole("admin");
        user.setInterest("develop");
        user.setUpdatedAt(LocalDateTime.now());

        User newUser = userRepository.save(user);

    }
}
