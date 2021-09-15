package com.example.digging.controller;

import com.example.digging.adapter.apple.AppleServiceImpl;
import com.example.digging.domain.entity.User;
import com.example.digging.domain.network.LoginDto;
import com.example.digging.domain.network.TokenDto;
import com.example.digging.domain.network.UserDto;
import com.example.digging.service.UserService;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Optional;

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


    @GetMapping(value = "/apple", params = { "id_token" })
    public String appleSUB(@RequestParam(name = "id_token") String idToken) {
        log.info(idToken);
        return appleImpl.getAppleSUBIdentity(idToken);
    }

    @PostMapping("/signup")
    public ResponseEntity<User> signup(
            @Valid @RequestBody UserDto userDto
    ) {
        return ResponseEntity.ok(userService.signup(userDto));
    }

    @PostMapping("/login")
    public ResponseEntity<TokenDto> login(@RequestBody LoginDto loginDto) {
        return ResponseEntity.ok(userService.login(loginDto));
    }

    @PostMapping(value = "/reissue", params = { "access_token", "refresh_token" })
    public ResponseEntity<TokenDto> reissue(@RequestParam(name = "access_token") String access_token, @RequestParam(name = "refresh_token") String refresh_token) {
        return ResponseEntity.ok(userService.reissue(access_token, refresh_token));
    }

    @GetMapping("/user")
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    public UserDto getMyUserInfo() {
        return userService.getUserInfoWithAutorities();
    }

//    @GetMapping("/user/{username}")
//    @PreAuthorize("hasAnyRole('ADMIN')")
//    public ResponseEntity<User> getUserInfo(@PathVariable String username) {
//        return ResponseEntity.ok(userService.getUserWithAuthorities(username).get());
//    }
}
