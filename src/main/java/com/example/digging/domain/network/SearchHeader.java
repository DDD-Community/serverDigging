package com.example.digging.domain.network;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SearchHeader<T> {

    private Integer totalPostsNum;

    private Integer totalPagesNum;

    private Integer pageNum;

    private Integer pageSize;

    private Integer nowSize;

    private Boolean isFirst;

    private Boolean isLast;

    private T postsList;

    //OK
    public static <T> SearchHeader<T> OK(Integer totalnum,Integer pNum, Integer pSize, Boolean first, Boolean last, T data) {
        Integer divres = (int)Math.ceil((double)totalnum / (double) 10);
        return (SearchHeader<T>) SearchHeader.builder()
                .totalPostsNum(totalnum)
                .totalPagesNum(divres)
                .pageNum(pNum)
                .pageSize(10)
                .nowSize(pSize)
                .isFirst(first)
                .isLast(last)
                .postsList(data)
                .build();
    }

    public static <T> SearchHeader<T> OK(Integer totalnum, T data) {
        Integer divres = (int)Math.ceil((double)totalnum / (double) 10);
        return (SearchHeader<T>) SearchHeader.builder()
                .totalPostsNum(totalnum)
                .totalPagesNum(divres)
                .pageSize(10)
                .postsList(data)
                .build();
    }

    public static <T> SearchHeader<T> NO() {
        return (SearchHeader<T>) SearchHeader.builder()
                .totalPostsNum(0)
                .postsList(new ArrayList<>())
                .build();
    }

    public static <T> SearchHeader<T> NO(Integer totalnum) {
        return (SearchHeader<T>) SearchHeader.builder()
                .totalPostsNum(totalnum)
                .postsList(new ArrayList<>())
                .build();
    }
}