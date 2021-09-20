package com.example.digging.domain.entity;

import lombok.*;
import lombok.experimental.Accessors;

import javax.persistence.*;
import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Builder
@Accessors(chain = true)
@ToString(exclude = {"postImg"})
public class Imgs {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String imgUrl;

    @ManyToOne
    @JoinColumn(name = "post_id", referencedColumnName = "posts_post_id")
    @JoinColumn(name = "img_id", referencedColumnName = "img_id")
    private PostImg postImg;
}
