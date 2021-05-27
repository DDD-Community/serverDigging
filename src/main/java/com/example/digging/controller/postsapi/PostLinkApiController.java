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
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;

@Slf4j
@RestController
@RequestMapping("/postlink")
@Api
public class PostLinkApiController implements CrudInterface<PostLinkApiRequest, PostLinkApiResponse> {
    @Autowired
    private PostLinkApiLogicService postLinkApiLogicService;

    @Override
    @PostMapping("")
    public Header<PostLinkApiResponse> create(@RequestBody Header<PostLinkApiRequest> request) {
        log.info("{}", request);
        return postLinkApiLogicService.create(request);
    }

    @Override
    public Header<PostLinkApiResponse> read(Integer id) {
        return null;
    }

    @GetMapping("")
    public Header<PostLinkReadResponse> linkread(@RequestParam(name = "userid") Integer userid, @RequestParam(name = "postid") Integer postid) {
        log.info("[READ LINK] user {} : post {}", userid, postid);
        return postLinkApiLogicService.linkread(userid, postid);
    }

    @GetMapping("/all_link_read")
    public Header<ArrayList<PostLinkReadResponse>> alllinkread(@RequestParam(name = "userid") Integer userid) {
        log.info("[READ All Link] user {} : post {}", userid);
        return postLinkApiLogicService.alllinkread(userid);
    }

    @Override
    public Header<PostLinkApiResponse> update(Integer id, Header<PostLinkApiRequest> request) {
        return null;
    }

    @Override
    public Header delete(Integer id) {
        return null;
    }
}
