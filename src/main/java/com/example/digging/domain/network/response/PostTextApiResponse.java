package com.example.digging.domain.network.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PostTextApiResponse {
    private String userName;
    private Integer postId;
    private Integer textId;
    private String title;
    private String content;
    private ArrayList<String> newTags;
}
