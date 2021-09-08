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

    @GetMapping(value = "/month/diggings", params = { "userid", "date" })
    public CalendarHeader<ArrayList<CalendarResponse>> calendarread(@RequestParam(name = "userid") Integer userid, @RequestParam(name = "date") String yearmonth) {
        log.info("[READ CALENDAR] user {} : yyyy-MM {}", userid, yearmonth);
        return CalendarService.calendarread(userid, yearmonth);
    }

    @GetMapping(value = "/month/diggings", params = { "userid" })
    public CalendarHeader<ArrayList<CalendarResponse>> calendarread(@RequestParam(name = "userid") Integer userid) {
        log.info("[READ CALENDAR] user {} : yyyy-MM {}", userid);
        return CalendarService.calendarread(userid);
    }

    @GetMapping(value = "/day/diggings", params = { "userid", "date" })
    public ArrayList<RecentDiggingResponse> calendarpostread(@RequestParam(name = "userid") Integer userid, @RequestParam(name = "date") String ymd) {
        log.info("[READ CALENDAR POST] user {} : yyyy-MM-dd {}", userid, ymd);
        return CalendarService.calendarpostread(userid, ymd);
    }

    @GetMapping(value = "/day/diggings", params = { "userid" })
    public ArrayList<RecentDiggingResponse> calendarpostread(@RequestParam(name = "userid") Integer userid) {
        log.info("[READ CALENDAR POST] user {}", userid);
        return CalendarService.calendarpostread(userid);
    }


}
