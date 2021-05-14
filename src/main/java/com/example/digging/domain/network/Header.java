package com.example.digging.domain.network;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Header {

    //api 통신 시간
    private String transactionTime;
    //api 통신 결과
    private String resultCode;
    //api 부가 설명
    private String description;
}
