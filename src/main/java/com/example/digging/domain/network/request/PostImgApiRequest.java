package com.example.digging.domain.network.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PostImgApiRequest {

    private String title;
    private List<String> tags;

    public String[] getTagsArr() {
        String[] tags = new String[getTags().size()];

        for (int i =0; i<getTags().size(); i++){
            tags[i] = getTags().get(i);
        }

        return tags;
    }
}