package com.example.digging.controller.postsapi;

import com.example.digging.domain.network.Header;
import com.example.digging.domain.network.request.PostTextApiRequest;
import com.example.digging.domain.network.response.PostLinkReadResponse;
import com.example.digging.domain.network.response.PostTextApiResponse;
import com.example.digging.domain.network.response.PostTextReadResponse;
import com.example.digging.ifs.CrudInterface;
import com.example.digging.service.PostLinkApiLogicService;
import com.example.digging.service.PostTextApiLogicService;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;

@Slf4j
@RestController
@RequestMapping("/posttext")
@Api
public class PostTextApiController implements CrudInterface<PostTextApiRequest, PostTextApiResponse> {

    @Autowired
    private PostTextApiLogicService postTextApiLogicService;

    @Override
    @PostMapping("")
    public PostTextApiResponse create(@RequestBody PostTextApiRequest request) {
        log.info("{}", request);
        return postTextApiLogicService.create(request);
    }

    @Override
    public PostTextApiResponse read(Integer id) {
        return null;
    }

    @Override
    public PostTextApiResponse update(Integer id, PostTextApiRequest request) {
        return null;
    }

    @GetMapping("")
    public PostTextReadResponse textread(@RequestParam(name = "userid") Integer userid, @RequestParam(name = "postid") Integer postid) {
        log.info("[READ Text] user {} : post {}", userid, postid);
        return postTextApiLogicService.textread(userid, postid);
    }

    @GetMapping("/all_text_read")
    public ArrayList<PostTextReadResponse> alltextread(@RequestParam(name = "userid") Integer userid) {
        log.info("[READ All Text] user {} : post {}", userid);
        return postTextApiLogicService.alltextread(userid);
    }

    @Override
    public PostTextApiResponse delete(Integer id) {
        return null;
    }
}
