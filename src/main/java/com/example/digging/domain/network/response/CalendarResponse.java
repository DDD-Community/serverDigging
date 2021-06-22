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
    private Boolean is_link;
    private Boolean is_img;
    private Boolean is_text;

}
