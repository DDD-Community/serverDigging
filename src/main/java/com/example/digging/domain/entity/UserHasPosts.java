package com.example.digging.domain.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@ToString(exclude = {"user", "posts"})
public class UserHasPosts {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne
    @JoinColumn(name="posts_post_id", referencedColumnName="post_id")
    private Posts posts;

    @ManyToOne
    @JoinColumn(name="user_id", referencedColumnName="id")
    private User user;

}
