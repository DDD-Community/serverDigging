package com.example.digging.domain.network.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserHasPostsApiResponse {

    private Integer id;
    private Integer userId;
    private String userName;
    private Integer postsPostId;
    private String postsType;

}
