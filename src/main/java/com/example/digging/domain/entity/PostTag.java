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
@ToString(exclude = {"posts", "tags"})
public class PostTag {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer Id;

    @ManyToOne
//    @JoinColumn(name = "post_id")
    private Posts posts;

    @ManyToOne
//    @JoinColumn(name = "tag_id")
    private Tags tags;
}
