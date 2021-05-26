package com.example.digging.controller.api;

import com.example.digging.domain.network.Header;
import com.example.digging.service.MainPageLogicService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;

@Slf4j
@RestController
@RequestMapping("")
public class MainPageController {
    @Autowired
    MainPageLogicService mainPageLogicService;

    @GetMapping("/recent")
    public Header recentPostsRead(Integer userid){
        return mainPageLogicService.recentPostsRead(userid);
    }
}
