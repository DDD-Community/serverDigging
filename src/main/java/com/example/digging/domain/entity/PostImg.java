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
@ToString(exclude = {"imgsList", "posts"})
public class PostImg {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int imgId;

    private String title;
    private LocalDateTime createdAt;
    private String createdBy;
    private LocalDateTime updatedAt;


    @OneToMany(fetch = FetchType.LAZY, mappedBy = "postImg", cascade = CascadeType.ALL)
    private List<Imgs> imgsList;

    @ManyToOne
    @JoinColumn(name = "post_id")
    private Posts posts;

}
