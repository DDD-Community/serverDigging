package com.example.digging.domain.entity;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import lombok.*;

import javax.persistence.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Builder
@ToString(exclude = {"postImg"})
public class Imgs {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String imgUrl;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumns({
            @JoinColumn(name="post_id", referencedColumnName="post_id"),
            @JoinColumn(name="img_id", referencedColumnName="img_id")
    })
    private PostImg postImg;
}
