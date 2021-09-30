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
public class PagingInfo {

    private Integer pageNum;

    private Integer nowSize;

    private Boolean isFirst;

    private Boolean isLast;

    private Integer startNum;
}
