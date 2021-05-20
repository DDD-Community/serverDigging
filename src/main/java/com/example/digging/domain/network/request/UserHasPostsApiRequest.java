package com.example.digging.domain.network.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserHasPostsApiRequest {

    private Integer id;
    private Integer userId;
    private Integer postsPostId;

}
