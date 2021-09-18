package com.example.digging.controller;

import com.example.digging.adapter.apple.AppleServiceImpl;
import com.example.digging.domain.entity.User;
import com.example.digging.domain.network.TokenDto;
import com.example.digging.domain.network.UserDto;
import com.example.digging.domain.network.response.ErrorResponse;
import com.example.digging.domain.network.response.GetPostNumByTypeResponse;
import com.example.digging.domain.network.response.PostsResponse;
import com.example.digging.domain.network.response.TotalTagResponse;
import com.example.digging.service.UserApiLogicService;
import com.example.digging.service.UserService;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Slf4j
@Api
@RestController
@RequestMapping("/api")
public class UserController {
    private final UserService userService;
    private final AppleServiceImpl appleImpl;

    public UserController(UserService userService, AppleServiceImpl appleImpl) {

        this.userService = userService;
        this.appleImpl = appleImpl;
    }


    @PostMapping(value = "/signup", params = { "id_token", "username", "email", "provider" })
    public ResponseEntity<User> signup(
            @Valid @RequestParam(name = "id_token") String idToken, @RequestParam(name = "username") String username,
            @RequestParam(name = "email") String email, @RequestParam(name = "provider") String provider
    ) {
        String oauthId = appleImpl.getAppleSUBIdentity(idToken);
        if (oauthId == "not valid id_token") {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ErrorResponse("id_token 오류 입니다. id_token 값이 유효하지 않습니다"));

        }
        return ResponseEntity.ok(userService.signup(oauthId, username, email, provider));
    }

    @GetMapping(value = "/login", params = { "username" })
    public ResponseEntity<TokenDto> login(@RequestParam(name = "username") String username) {
        return ResponseEntity.ok(userService.login(username));
    }

    @GetMapping(value = "/reissue", params = { "access_token", "refresh_token" })
    public ResponseEntity<TokenDto> reissue(@RequestParam(name = "access_token") String access_token, @RequestParam(name = "refresh_token") String refresh_token) {
        return ResponseEntity.ok(userService.reissue(access_token, refresh_token));
    }

    @GetMapping("/user")
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    public UserDto getMyUserInfo() {
        return userService.getUserInfoWithAutorities();
    }

    @GetMapping("/get_total_tags")
    public TotalTagResponse getUserTotalTags() {
        log.info("user Total tags ");
        return userService.getUserTotalTags();
    }

    @PutMapping("/set_like")
    public PostsResponse setLike(@RequestParam(name = "postid") Integer postid) {
        log.info("setLike : {}", postid);
        return userService.setLike(postid);
    }

    @GetMapping("/posts_num_bytype")
    public GetPostNumByTypeResponse getPostNumByType() {
        log.info("[User] Get Post Num By Type");
        return userService.getPostNumByType();
    }

    @DeleteMapping("/delete_post")
    public PostsResponse deletePost(@RequestParam(name = "postid") Integer postid) {
        log.info("delete id : {}", postid);
        return userService.deletePost(postid);
    }

}
