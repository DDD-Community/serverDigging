package com.example.digging.domain.network.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.ArrayList;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CalendarResponse {

    private Integer id;
    private String resultCode;
    private String day;
    private LocalDate date;
    private Integer is_link;
    private Integer is_img;
    private Integer is_text;

}
