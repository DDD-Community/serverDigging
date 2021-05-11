package com.example.digging.domain.repository;

import com.example.digging.DiggingApplicationTests;
import com.example.digging.domain.entity.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDateTime;
import java.util.Optional;

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


        User newUser = userRepository.save(user);
        System.out.println(newUser);
    }

    @Test
    public void read(){
        Optional<User> user = userRepository.findById(2);

        user.ifPresent(selectUser -> System.out.println("user : "+ selectUser));
    }

    @Test
    public void update(){
        Optional<User> user = userRepository.findById(2);

        user.ifPresent(selectUser -> {
            selectUser.setUsername("updateUser");
            selectUser.setUpdatedAt(LocalDateTime.now());

            userRepository.save(selectUser);
        });
    }

    @Test
    public void delete() {
        Optional<User> user = userRepository.findById(4);

        user.ifPresent(selectUser ->{
            userRepository.delete(selectUser);
        });

        Optional<User> deleteUser = userRepository.findById(4);

        if(deleteUser.isPresent()){
            System.out.println("데이터 존재");
        }else{
            System.out.println("데이터 없음");
        }

    }
}
