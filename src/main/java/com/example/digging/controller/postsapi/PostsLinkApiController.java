package com.example.digging.controller.postsapi;

import com.example.digging.domain.network.Header;
import com.example.digging.domain.network.request.PostLinkApiRequest;
import com.example.digging.domain.network.response.PostLinkApiResponse;
import com.example.digging.ifs.CrudInterface;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/postlink")
public class PostsLinkApiController implements CrudInterface<PostLinkApiRequest, PostLinkApiResponse> {
    @Override
    public Header<PostLinkApiResponse> create(Header<PostLinkApiRequest> request) {
        return null;
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
