package com.example.digging.domain.network.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PostImgReadResponse {
    private String resultCode;
    private String type;
    private Integer postId;
    private Integer imgId;
    private String title;
    private LocalDateTime createdAt;
    private String createdBy;
    private LocalDateTime updatedAt;
    private String updatedBy;
    private Boolean isLike;
    private Integer totalImgNum;
    private ArrayList<String> tags;
    private ArrayList<ImgsApiResponse> imgs;
}
