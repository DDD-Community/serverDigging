package com.example.digging.controller.api;

import com.example.digging.domain.network.Header;
import com.example.digging.domain.network.request.CheckUserRequest;
import com.example.digging.domain.network.request.SetLikeRequest;
import com.example.digging.domain.network.request.UserApiRequest;
import com.example.digging.domain.network.response.UserApiResponse;
import com.example.digging.ifs.CrudInterface;
import com.example.digging.service.UserApiLogicService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/api/user")
public class UserApiController implements CrudInterface<UserApiRequest, UserApiResponse> {

    @Autowired
    private UserApiLogicService userApiLogicService;

    @Override
    @PostMapping("")
    public Header<UserApiResponse> create(@RequestBody Header<UserApiRequest> request) {
        log.info("{}", request);
        return userApiLogicService.create(request);
    }

    @Override
    @GetMapping("")
    public Header<UserApiResponse> read(@RequestParam("id") Integer id) {
        log.info("read id : {}", id);
        return userApiLogicService.read(id);
    }

    @Override
    @PutMapping("/{id}")
    public Header<UserApiResponse> update(@PathVariable(name = "id") Integer id, @RequestBody Header<UserApiRequest> request) {
        log.info("update id : {}", id);
        log.info("update request : {}", request);
        return userApiLogicService.update(id, request);
    }

    @Override
    @DeleteMapping("/{id}")
    public Header delete(@PathVariable(name = "id") Integer id) {
        log.info("delete id : {}", id);
        return userApiLogicService.delete(id);
    }

    @GetMapping("/get_total_tags")
    public Header getUserTotalTags(@RequestParam(name = "userid") Integer id) {
        log.info("user Total tags : {}", id);
        return userApiLogicService.getUserTotalTags(id);
    }

    @PutMapping("/set_like")
    public Header setLike(@RequestBody Header<SetLikeRequest> request) {
        log.info("setLike : {}", request);
        return userApiLogicService.setLike(request);
    }

}
