package com.example.digging.controller.api;

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

import java.util.ArrayList;

@Slf4j
@RestController
@RequestMapping("/calendar")
public class CalendarController {

    @Autowired
    private CalendarLogicService CalendarService;

    @GetMapping("/allcheck")
    public ArrayList<CalendarResponse> calendarread(@RequestParam(name = "userid") Integer userid, @RequestParam(name = "yyyyMM") String yearmonth) {
        log.info("[READ CALENDAR] user {} : yyyy-MM {}", userid, yearmonth);
        return CalendarService.calendarread(userid, yearmonth);
    }

    @GetMapping("/readdaypost")
    public ArrayList<RecentDiggingResponse> calendarpostread(@RequestParam(name = "userid") Integer userid, @RequestParam(name = "yyyyMMdd") String ymd) {
        log.info("[READ CALENDAR POST] user {} : yyyy-MM-dd {}", userid, ymd);
        return CalendarService.calendarpostread(userid, ymd);
    }
}