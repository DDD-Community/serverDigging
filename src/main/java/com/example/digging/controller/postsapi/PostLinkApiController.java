package com.example.digging.controller.postsapi;

import com.example.digging.domain.network.Header;
import com.example.digging.domain.network.request.PostLinkApiRequest;
import com.example.digging.domain.network.request.UserApiRequest;
import com.example.digging.domain.network.response.PostLinkApiResponse;
import com.example.digging.domain.network.response.UserApiResponse;
import com.example.digging.ifs.CrudInterface;
import com.example.digging.service.PostLinkApiLogicService;
import com.example.digging.service.UserApiLogicService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/postlink")
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

    @Override
    public Header<PostLinkApiResponse> update(Integer id, Header<PostLinkApiRequest> request) {
        return null;
    }

    @Override
    public Header delete(Integer id) {
        return null;
    }
}
