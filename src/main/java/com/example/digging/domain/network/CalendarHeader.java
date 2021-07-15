package com.example.digging.domain.network;

import com.example.digging.domain.network.response.CalendarResponse;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CalendarHeader<T> {

    //month_info
    private String monthInfo;

    private T data;

    //OK
    public static <T> CalendarHeader<T> OK(String info,T data) {
        return (CalendarHeader<T>) CalendarHeader.builder()
                .monthInfo(info)
                .data(data)
                .build();
    }
}
