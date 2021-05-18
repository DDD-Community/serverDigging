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
@ToString(exclude = {"posts", "tags"})
public class PostTag {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int Id;

    @ManyToOne
    @JoinColumn(name = "post_id")
    private Posts posts;

    @ManyToOne
    @JoinColumn(name = "tag_id")
    private Tags tags;
}
