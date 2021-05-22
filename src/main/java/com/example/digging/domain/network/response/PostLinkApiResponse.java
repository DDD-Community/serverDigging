package com.example.digging.domain.network.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PostLinkApiResponse {
    private String userName;
    private Integer postId;
    private Integer linkId;
    private String title;
    private String urlCheck; //유효한 주소인 지 여부
    private String[] tags;
}
