package com.example.digging.controller.postsapi;

import com.example.digging.domain.network.Header;
import com.example.digging.domain.network.request.PostLinkApiRequest;
import com.example.digging.domain.network.request.UserApiRequest;
import com.example.digging.domain.network.response.PostLinkApiResponse;
import com.example.digging.domain.network.response.PostLinkReadResponse;
import com.example.digging.domain.network.response.PostTextReadResponse;
import com.example.digging.domain.network.response.UserApiResponse;
import com.example.digging.ifs.CrudInterface;
import com.example.digging.service.PostLinkApiLogicService;
import com.example.digging.service.UserApiLogicService;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;

@Slf4j
@RestController
@RequestMapping("/postlink")
@Api
public class PostLinkApiController {
    @Autowired
    private PostLinkApiLogicService postLinkApiLogicService;


    @PostMapping("")
    public ResponseEntity<PostLinkApiResponse> create(@RequestBody PostLinkApiRequest request) {
        log.info("{}", request);
        return postLinkApiLogicService.create(request);
    }


    @GetMapping("")
    public ResponseEntity<PostLinkReadResponse> linkread(@RequestParam(name = "postid") Integer postid) {
        log.info("[READ LINK] : post {}", postid);
        return postLinkApiLogicService.linkread(postid);
    }

    @GetMapping("/all_link_read")
    public ResponseEntity<ArrayList<PostLinkReadResponse>> alllinkread() {
        log.info("[READ All Link]" );
        return postLinkApiLogicService.alllinkread();
    }

}
