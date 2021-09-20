package com.example.digging.domain.network;

import com.example.digging.domain.entity.PostTag;
import com.example.digging.domain.entity.Posts;
import lombok.*;

import java.util.List;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Search {
    private String title = "";
    private List<PostTag> tagsList;
    private String content = "";
    private Posts addPost;
    private int str_count = 0;
}
