package com.example.digging.controller.api;

import com.example.digging.domain.network.Header;
import com.example.digging.domain.network.request.UserHasPostsApiRequest;
import com.example.digging.domain.network.response.UserHasPostsApiResponse;
import com.example.digging.ifs.CrudInterface;
import com.example.digging.service.UserHasPostsApiLogicService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/api/userHasPosts")
public class UserHasPostsApiController implements CrudInterface<UserHasPostsApiRequest, UserHasPostsApiResponse> {

    @Autowired
    private UserHasPostsApiLogicService userHasPostsApiLogicService;

    @Override
    @PostMapping
    public Header<UserHasPostsApiResponse> create(@RequestBody Header<UserHasPostsApiRequest> request) {

        return userHasPostsApiLogicService.create(request);
    }

    @Override
    @GetMapping("/{id}")
    public Header<UserHasPostsApiResponse> read(@PathVariable Integer id) {
        return userHasPostsApiLogicService.read(id);
    }

    @Override
    @PutMapping("/{id}")
    public Header<UserHasPostsApiResponse> update(@PathVariable Integer id,@RequestBody Header<UserHasPostsApiRequest> request) {
        return null;
    }

    @Override
    @DeleteMapping("/{id}")
    public Header delete(@PathVariable Integer id) {
        return null;
    }
}
