package com.example.digging.domain.network.response;

import com.example.digging.domain.entity.Tags;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TotalTagResponse {
    private String resultCode;
    private int totalNum;
    private ArrayList<String> totalTags;
}
