package com.example.digging.domain.entity;

import lombok.*;
import lombok.experimental.Accessors;

import javax.persistence.*;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Builder
@Accessors(chain = true)
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
//    @JoinColumn(name = "post_id")
    private Posts posts;
}
