package com.example.digging.controller;

import com.example.digging.adapter.apple.AppleServiceImpl;
import com.example.digging.adapter.google.GoogleServiceImpl;
import com.example.digging.domain.entity.User;
import com.example.digging.domain.network.TokenDto;
import com.example.digging.domain.network.UserDto;
import com.example.digging.domain.network.request.LoginRequest;
import com.example.digging.domain.network.request.PostLinkApiRequest;
import com.example.digging.domain.network.request.SignupRequest;
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
    private final GoogleServiceImpl googleImpl;

    public UserController(UserService userService, AppleServiceImpl appleImpl, GoogleServiceImpl googleImpl) {

        this.userService = userService;
        this.appleImpl = appleImpl;
        this.googleImpl = googleImpl;
    }


    @PostMapping(value = "/signup")
    public ResponseEntity<TokenDto> signup(
            @Valid @RequestBody SignupRequest request
    ) {
        if (request.getProvider().equals("apple")) {
            String uid = appleImpl.getAppleSUBIdentity(request.getToken());
            String useremail = appleImpl.getAppleEmailIdentity(request.getToken());
            if (uid == "not valid id_token") {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(new ErrorResponse("id_token 오류 입니다. id_token 값이 유효하지 않습니다", "404"));

            }
            return userService.signup(uid, useremail, request.getProvider());
        } else {
            String uid = googleImpl.verifyAndGetUid(request.getToken());
            String useremail = googleImpl.verifyAndGetEmail(request.getToken());
            if (uid == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(new ErrorResponse("access_token 오류 입니다. UID를 로드할 수 없습니다.", "404"));
            }

            return userService.signup(uid, useremail, request.getProvider());
        }

    }

    @PostMapping(value = "/login")
    public ResponseEntity<TokenDto> login(@RequestBody LoginRequest request) {
        return userService.login(request);
    }

    @GetMapping(value = "/reissue", params = { "access_token", "refresh_token" })
    public ResponseEntity<TokenDto> reissue(@RequestParam(name = "access_token") String access_token, @RequestParam(name = "refresh_token") String refresh_token) {
        return userService.reissue(access_token, refresh_token);
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
