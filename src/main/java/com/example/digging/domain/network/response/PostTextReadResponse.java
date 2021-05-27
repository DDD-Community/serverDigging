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
public class PostTextReadResponse {
    private String resultCode;
    private Integer postId;
    private Integer textId;
    private String title;
    private String content;
    private LocalDateTime createdAt;
    private String createdBy;
    private LocalDateTime updatedAt;
    private String updatedBy;
    private Boolean isLike;
    private ArrayList<String> tags;
}
