package com.example.digging.controller.api;

import com.example.digging.domain.network.CalendarHeader;
import com.example.digging.domain.network.response.CalendarResponse;
import com.example.digging.domain.network.response.PostLinkReadResponse;
import com.example.digging.domain.network.response.RecentDiggingResponse;
import com.example.digging.service.CalendarLogicService;
import com.example.digging.service.PostLinkApiLogicService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.lang.reflect.Array;
import java.util.ArrayList;

@Slf4j
@RestController
@RequestMapping("/calendar")
public class CalendarController {

    @Autowired
    private CalendarLogicService CalendarService;

    @GetMapping(value = "/month/diggings", params = { "date" })
    public CalendarHeader<ArrayList<CalendarResponse>> calendarread(@RequestParam(name = "date") String yearmonth) {
        log.info("[READ CALENDAR] : yyyy-MM {}", yearmonth);
        return CalendarService.calendarread(yearmonth);
    }

    @GetMapping(value = "/month/diggings")
    public CalendarHeader<ArrayList<CalendarResponse>> calendarread() {
        log.info("[READ CALENDAR] : yyyy-MM {}");
        return CalendarService.calendarread();
    }

    @GetMapping(value = "/day/diggings", params = { "date" })
    public ArrayList<RecentDiggingResponse> calendarpostread(@RequestParam(name = "date") String ymd) {
        log.info("[READ CALENDAR POST] : yyyy-MM-dd {}", ymd);
        return CalendarService.calendarpostread(ymd);
    }

    @GetMapping(value = "/day/diggings")
    public ArrayList<RecentDiggingResponse> calendarpostread() {
        log.info("[READ CALENDAR POST]" );
        return CalendarService.calendarpostread();
    }


}
