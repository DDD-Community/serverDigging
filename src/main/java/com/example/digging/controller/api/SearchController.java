package com.example.digging.controller.api;

import com.example.digging.domain.network.CalendarHeader;
import com.example.digging.domain.network.SearchHeader;
import com.example.digging.domain.network.response.CalendarResponse;
import com.example.digging.domain.network.response.RecentDiggingResponse;
import com.example.digging.service.SearchLogicService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;

@Slf4j
@RestController
@RequestMapping("/search")
public class SearchController {

    @Autowired
    private SearchLogicService searchLogicService;

    @GetMapping(value = "/keyword", params = { "keyword" })
    public SearchHeader<ArrayList<RecentDiggingResponse>> searchbykeyword(@RequestParam(name = "keyword") String keyword) {
        log.info("[SEARCH by Keyword] : {}", keyword);
        return searchLogicService.searchByKeyword(keyword);
    }

    @GetMapping(value = "/tag", params = { "tag" })
    public SearchHeader<ArrayList<RecentDiggingResponse>> searchbytag(@RequestParam(name = "tag") String tag) {
        log.info("[SEARCH by tag] : {}", tag);
        return searchLogicService.searchByTag(tag);
    }
}
