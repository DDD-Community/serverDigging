package com.example.digging.controller.api;

import com.example.digging.domain.network.Header;
import com.example.digging.domain.network.request.PostsApiRequest;
import com.example.digging.domain.network.response.PostsApiResponse;
import com.example.digging.ifs.CrudInterface;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/api/posts")
public class PostsApiController implements CrudInterface<PostsApiRequest, PostsApiResponse> {
    @Override
    @PostMapping("")
    public Header<PostsApiResponse> create(@RequestBody Header<PostsApiRequest> request) {
        return null;
    }

    @Override
    @GetMapping("/{id}")
    public Header<PostsApiResponse> read(@PathVariable Integer id) {
        return null;
    }

    @Override
    @PutMapping("/{id}")
    public Header<PostsApiResponse> update(@PathVariable Integer id,@RequestBody Header<PostsApiRequest> request) {
        return null;
    }

    @Override
    @DeleteMapping("/{id}")
    public Header delete(@PathVariable Integer id) {
        return null;
    }
}
