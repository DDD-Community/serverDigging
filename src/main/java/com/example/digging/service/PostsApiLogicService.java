package com.example.digging.service;

import com.example.digging.domain.entity.User;
import com.example.digging.domain.network.Header;
import com.example.digging.domain.network.request.PostsApiRequest;
import com.example.digging.domain.network.response.PostsApiResponse;
import com.example.digging.ifs.PostsCrudInterface;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class PostsApiLogicService implements PostsCrudInterface<PostsApiRequest, PostsApiResponse> {

    @Override
    public Header<PostsApiRequest> create(Integer userId, String type) {
        return null;
    }

    @Override
    public Header<PostsApiRequest> read(Integer id) {
        return null;
    }

    @Override
    public Header<PostsApiRequest> update(Integer id) {
        return null;
    }

    @Override
    public Header delete(Integer id) {
        return null;
    }
}
