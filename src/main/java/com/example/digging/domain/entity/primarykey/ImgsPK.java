package com.example.digging.domain.entity.primarykey;


import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.NonNull;

import javax.persistence.*;
import java.io.Serializable;

@Embeddable
@NoArgsConstructor
@AllArgsConstructor
public class ImgsPK implements Serializable {
    @NonNull
    @Column(name = "post_id", nullable = false, updatable = false)
    private int postId;

    @NonNull
    @Column(name = "img_id", nullable = false, updatable = false)
    private int imgId;

    @NonNull
    @Column(name = "id", nullable = false, updatable = false)
    private int id;
}
