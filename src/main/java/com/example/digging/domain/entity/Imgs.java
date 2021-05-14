package com.example.digging.domain.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Imgs {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int postId;

//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int imgId;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String imgUrl;

}
