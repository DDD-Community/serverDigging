package com.example.digging.controller.postsapi;

import com.example.digging.domain.network.Header;
import com.example.digging.domain.network.request.PostTextApiRequest;
import com.example.digging.domain.network.response.PostTextApiResponse;
import com.example.digging.ifs.CrudInterface;
import com.example.digging.service.PostLinkApiLogicService;
import com.example.digging.service.PostTextApiLogicService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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

    @Override
    public Header delete(Integer id) {
        return null;
    }
}
