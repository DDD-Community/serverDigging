package com.example.digging.domain.network.response;

import com.example.digging.domain.entity.Posts;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RecentDiggingResponse {
    private int totalNum;
    private ArrayList<Posts> totalTags;
}
