package com.example.digging.controller;

import com.example.digging.domain.network.Header;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class DiggingController {

    @RequestMapping(method = RequestMethod.GET, path = "getMethod")
    public String getRequest() {
        return "Get Method Test2";
    }

    @GetMapping("/header")
    public Header getHeader() {
        return Header.builder().resultCode("OK").description("OK").build();
    }
}
