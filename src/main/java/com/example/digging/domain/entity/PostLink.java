package com.example.digging.domain.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.*;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@ToString(exclude = {"posts"})
public class PostLink {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int linkId;

    private String title;
    private String url;
    private LocalDateTime createdAt;
    private String createdBy;
    private LocalDateTime updatedAt;
    private String updatedBy;

    @ManyToOne
    @JoinColumn(name = "postId")
    private Posts posts;
}
