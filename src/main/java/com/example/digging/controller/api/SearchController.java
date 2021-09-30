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
    public SearchHeader<ArrayList<RecentDiggingResponse>> searchByKeyword(@RequestParam(name = "keyword") String keyword) {
        log.info("[SEARCH by Keyword] : {}", keyword);
        return searchByKeywordWithPage(keyword, 1);
    }

    @GetMapping(value = "/keyword", params = { "keyword", "page" })
    public SearchHeader<ArrayList<RecentDiggingResponse>> searchByKeywordWithPage(@RequestParam(name = "keyword") String keyword, @RequestParam(name = "page") Integer page) {
        log.info("[SEARCH by Keyword] : {}", keyword, page);
        System.out.println(page);
        return searchLogicService.searchByKeyword(keyword, page);
    }

    @GetMapping(value = "/tag", params = { "tag" })
    public SearchHeader<ArrayList<RecentDiggingResponse>> searchByTag(@RequestParam(name = "tag") String tag) {
        log.info("[SEARCH by tag] : {}", tag);
        return searchByTagWithPage(tag, 1);
    }

    @GetMapping(value = "/tag", params = { "tag", "page" })
    public SearchHeader<ArrayList<RecentDiggingResponse>> searchByTagWithPage(@RequestParam(name = "tag") String tag, @RequestParam(name = "page") Integer page) {
        log.info("[SEARCH by tag] : {}", tag);
        return searchLogicService.searchByTag(tag, page);
    }
}
