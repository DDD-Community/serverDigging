package com.example.digging.controller.api;

import com.example.digging.domain.network.response.CalendarResponse;
import com.example.digging.domain.network.response.PostLinkReadResponse;
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

    @GetMapping("/read")
    public ArrayList<CalendarResponse> calendarread(@RequestParam(name = "userid") Integer userid, @RequestParam(name = "yearmonth") String yearmonth) {
        log.info("[READ LINK] user {} : post {}", userid, yearmonth);
        return CalendarService.calendarread(userid, yearmonth);
    }
}
