package com.example.digging.domain.entity;

import com.example.digging.domain.entity.primarykey.ImgsPK;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Builder
public class Imgs {

//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    private int postId;
//
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    private int imgId;
//
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    private int id;

    @EmbeddedId
    private ImgsPK imgPK;
    

    private String imgUrl;

}
