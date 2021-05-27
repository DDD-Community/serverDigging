package com.example.digging.domain.network.response;

import com.example.digging.domain.network.Header;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PostsResponse {

    private String resultCode;
    private String type;
    private Integer postId;
    private Boolean isText;
    private Boolean isImg;
    private Boolean isLink;
    private Boolean isLike;
    private LocalDateTime createdAt;
    private String createdBy;
    private LocalDateTime updatedAt;
    private String updatedBy;

}
