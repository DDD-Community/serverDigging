package com.example.digging.domain.entity;

import lombok.AllArgsConstructor;
import lombok.Data;

import javax.persistence.*;
import java.time.LocalDateTime;


@Data
@AllArgsConstructor
@Entity
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String username;
    private String email;
    private String password;
    private String provider;
    private String role;
    private String interest;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public User() {

    }
}
