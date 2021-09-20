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
public class PostImgApiResponse {

    private String resultCode;
    private String type;
    private String userName;
    private Integer postId;
    private Integer imgId;
    private String title;
    private ArrayList<String> newTags;
    private ArrayList<ImgsApiResponse> imgs;

}
