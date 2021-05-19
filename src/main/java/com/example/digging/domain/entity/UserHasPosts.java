package com.example.digging.domain.entity;

import lombok.*;
import lombok.experimental.Accessors;

import javax.persistence.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Builder
@Accessors(chain = true)
@ToString(exclude = {"user", "posts"})
public class UserHasPosts {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne
//    @JoinColumn(name="posts_post_id", referencedColumnName="post_id")
    private Posts posts;

    @ManyToOne
//    @JoinColumn(name="user_id", referencedColumnName="id")
    private User user;

}
