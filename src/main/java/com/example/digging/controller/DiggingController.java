package com.example.digging.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class DiggingController {

    @RequestMapping(method = RequestMethod.GET, path = "getMethod")
    public String getRequest() {
        return "Get Method Test";
    }

}
