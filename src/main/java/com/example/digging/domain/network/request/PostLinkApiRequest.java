package com.example.digging.domain.network.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PostLinkApiRequest {
    private String userName;
    private Integer userId;
    private String title;
    private String url;
    private List tags;
}
