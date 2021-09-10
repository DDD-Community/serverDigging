package com.example.digging.service;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import com.example.digging.domain.entity.Authority;
import com.example.digging.domain.entity.Tags;
import com.example.digging.domain.entity.User;
import com.example.digging.domain.network.TokenDto;
import com.example.digging.domain.network.exception.DuplicateMemberException;
import com.example.digging.domain.network.UserDto;
import com.example.digging.domain.network.response.UserApiResponse;
import com.example.digging.domain.repository.UserRepository;
import com.example.digging.util.SecurityUtil;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional
    public User signup(UserDto userDto) {
        if (userRepository.findOneWithAuthoritiesByOauthId(userDto.getOauthId()).orElse(null) != null) {
            throw new DuplicateMemberException("이미 가입되어 있는 유저입니다.");
        }

        //빌더 패턴의 장점
        Authority authority = Authority.builder()
                .authorityName("ROLE_USER")
                .build();

        String makeNewUsername;
        List<User> sameUsernameList = userRepository.findByUsernameStartsWith(userDto.getUsername());
        int sameUsernameNum = sameUsernameList.size();
        if (sameUsernameNum > 0 ) {
            makeNewUsername = userDto.getUsername() +  Integer.toString(sameUsernameNum + 1);
        } else {
            makeNewUsername = userDto.getUsername();
        }

        User user = User.builder()
                .username(makeNewUsername)
                .password(passwordEncoder.encode(userDto.getPassword()))
                .email(userDto.getEmail())
                .provider(userDto.getProvider())
                .interest(userDto.getInterest())
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .oauthId(userDto.getOauthId())
                .authorities(Collections.singleton(authority))
                .activated(true)
                .build();

        return userRepository.save(user);
    }


    @Transactional(readOnly = true)
    public Optional<User> getUserWithAuthorities(String username) {
        return userRepository.findOneWithAuthoritiesByUsername(username);
    }

    @Transactional(readOnly = true)
    public Optional<User> getMyUserWithAuthorities() {
        return SecurityUtil.getCurrentUsername().flatMap(userRepository::findOneWithAuthoritiesByUsername);
    }
}