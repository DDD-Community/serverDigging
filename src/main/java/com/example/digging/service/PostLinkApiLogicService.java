package com.example.digging.service;

import com.example.digging.domain.network.Header;
import com.example.digging.domain.network.request.PostLinkApiRequest;
import com.example.digging.domain.network.response.PostLinkApiResponse;
import com.example.digging.ifs.CrudInterface;
import org.springframework.stereotype.Service;

@Service
public class PostLinkApiLogicService implements CrudInterface<PostLinkApiRequest, PostLinkApiResponse> {
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
