package com.example.digging.domain.network.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class GetPostNumByTypeResponse {

    private String resultCode;
    private String userName;
    private Integer totalText;
    private Integer totalImg;
    private Integer totalLink;
}
