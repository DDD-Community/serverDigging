package com.example.digging.domain.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@ToString(exclude = {"userHasPosts"})
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

//    @OneToMany(fetch = FetchType.LAZY, mappedBy = "user")
//    private List<UserHasPosts> userHasPostsList;

}
