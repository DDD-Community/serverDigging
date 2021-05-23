package com.example.digging.domain.repository;

import com.example.digging.DiggingApplicationTests;
import com.example.digging.domain.entity.PostImg;
import com.example.digging.domain.entity.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDateTime;

public class PostImgRepositoryTest extends DiggingApplicationTests {

    // Dependency Injection
    @Autowired
    private PostImgRepository postImgRepository;

    @Test
    public void create(){

    }
}
