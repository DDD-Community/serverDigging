package com.example.digging.domain.network;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SearchHeader<T> {

    //month_info
    private Integer totalPostsNum;

    private T postsList;

    //OK
    public static <T> SearchHeader<T> OK(Integer totalnum,T data) {
        return (SearchHeader<T>) SearchHeader.builder()
                .totalPostsNum(totalnum)
                .postsList(data)
                .build();
    }

    public static <T> SearchHeader<T> NO() {
        return (SearchHeader<T>) SearchHeader.builder()
                .totalPostsNum(0)
                .build();
    }
}