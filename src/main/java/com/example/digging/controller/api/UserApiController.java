package com.example.digging.controller.api;

import com.example.digging.domain.network.Header;
import com.example.digging.domain.network.request.CheckUserRequest;
import com.example.digging.domain.network.request.SetLikeRequest;
import com.example.digging.domain.network.request.UserApiRequest;
import com.example.digging.domain.network.response.GetPostNumByTypeResponse;
import com.example.digging.domain.network.response.PostsResponse;
import com.example.digging.domain.network.response.TotalTagResponse;
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
    public UserApiResponse create(@RequestBody UserApiRequest request) {
        log.info("{}", request);
        return userApiLogicService.create(request);
    }

    @Override
    @GetMapping("")
    public UserApiResponse read(@RequestParam("id") Integer id) {
        log.info("read id : {}", id);
        return userApiLogicService.read(id);
    }

    @Override
    @PutMapping("/{id}")
    public UserApiResponse update(@PathVariable(name = "id") Integer id, @RequestBody UserApiRequest request) {
        log.info("update id : {}", id);
        log.info("update request : {}", request);
        return userApiLogicService.update(id, request);
    }

    @Override
    @DeleteMapping("/{id}")
    public UserApiResponse delete(@PathVariable(name = "id") Integer id) {
        log.info("delete id : {}", id);
        return userApiLogicService.delete(id);
    }

    @GetMapping("/get_total_tags")
    public TotalTagResponse getUserTotalTags(@RequestParam(name = "userid") Integer id) {
        log.info("user Total tags : {}", id);
        return userApiLogicService.getUserTotalTags(id);
    }

    @PutMapping("/set_like")
    public PostsResponse setLike(@RequestBody SetLikeRequest request) {
        log.info("setLike : {}", request);
        return userApiLogicService.setLike(request);
    }

    @GetMapping("/posts_num_bytype")
    public GetPostNumByTypeResponse getPostNumByType(@RequestParam(name = "userid") Integer id) {
        log.info("[User {} ] Get Post Num By Type", id);
        return userApiLogicService.getPostNumByType(id);
    }

}
