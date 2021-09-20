package com.example.digging.domain.entity;

import lombok.*;
import lombok.experimental.Accessors;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Builder
@Accessors(chain = true)
@ToString(exclude = {"imgsList", "posts"})
public class PostImg {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "img_id")
    private Integer imgId;

    private String title;
    private LocalDateTime createdAt;
    private String createdBy;
    private LocalDateTime updatedAt;
    private String updatedBy;


    @OneToMany(fetch = FetchType.LAZY, mappedBy = "postImg", cascade = CascadeType.ALL)
    private List<Imgs> imgsList;

    @ManyToOne
//    @JoinColumn(name = "post_id")
    private Posts posts;

}
