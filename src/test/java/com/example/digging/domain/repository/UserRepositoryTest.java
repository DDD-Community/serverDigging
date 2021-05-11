package com.example.digging.domain.repository;

import com.example.digging.DiggingApplicationTests;
import com.example.digging.domain.entity.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDateTime;

public class UserRepositoryTest extends DiggingApplicationTests {

    // Dependency Injection
    @Autowired
    private UserRepository userRepository;

    @Test
    public void create(){

        User user = new User();

        user.setEmail("gmail");
        user.setPassword("pass");
        user.setUsername("test2");
        user.setProvider("admin");
        user.setRole("admin");
        user.setInterest("dev");
        user.setCreatedAt(LocalDateTime.now());
        user.setUpdatedAt(LocalDateTime.now());
        System.out.println(user);

        User newUser = userRepository.save(user);

    }

//    public void read(){
//        Optional<User> user = userRepository.findAll();
//
//        user.ifPresent(user ->)
//    }
}
