package com.example.digging.controller.postsapi;

import com.example.digging.domain.network.Header;
import com.example.digging.domain.network.request.PostTextApiRequest;
import com.example.digging.domain.network.response.PostLinkReadResponse;
import com.example.digging.domain.network.response.PostTextApiResponse;
import com.example.digging.domain.network.response.PostTextReadResponse;
import com.example.digging.ifs.CrudInterface;
import com.example.digging.service.PostLinkApiLogicService;
import com.example.digging.service.PostTextApiLogicService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/posttext")
public class PostTextApiController implements CrudInterface<PostTextApiRequest, PostTextApiResponse> {

    @Autowired
    private PostTextApiLogicService postTextApiLogicService;

    @Override
    @PostMapping("")
    public Header<PostTextApiResponse> create(@RequestBody Header<PostTextApiRequest> request) {
        log.info("{}", request);
        return postTextApiLogicService.create(request);
    }

    @Override
    public Header<PostTextApiResponse> read(Integer id) {
        return null;
    }

    @Override
    public Header<PostTextApiResponse> update(Integer id, Header<PostTextApiRequest> request) {
        return null;
    }

    @GetMapping("")
    public Header<PostTextReadResponse> textread(@RequestParam(name = "userid") Integer userid, @RequestParam(name = "postid") Integer postid) {
        log.info("[READ Text] user {} : post {}", userid, postid);
        return postTextApiLogicService.textread(userid, postid);
    }

    @Override
    public Header delete(Integer id) {
        return null;
    }
}
