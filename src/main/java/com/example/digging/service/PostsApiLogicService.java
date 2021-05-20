package com.example.digging.service;

import com.example.digging.domain.network.Header;
import com.example.digging.domain.network.request.PostsApiRequest;
import com.example.digging.domain.network.response.PostsApiResponse;
import com.example.digging.ifs.CrudInterface;
import org.springframework.stereotype.Service;

@Service
public class PostsApiLogicService implements CrudInterface<PostsApiRequest, PostsApiResponse> {
    @Override
    public Header<PostsApiResponse> create(Header<PostsApiRequest> request) {
        return null;
    }

    @Override
    public Header<PostsApiResponse> read(Integer id) {
        return null;
    }

    @Override
    public Header<PostsApiResponse> update(Integer id, Header<PostsApiRequest> request) {
        return null;
    }

    @Override
    public Header delete(Integer id) {
        return null;
    }
}
