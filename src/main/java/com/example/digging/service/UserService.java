package com.example.digging.service;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import com.example.digging.adapter.jwt.TokenProvider;
import com.example.digging.domain.entity.Authority;
import com.example.digging.domain.entity.RefreshToken;
import com.example.digging.domain.entity.Tags;
import com.example.digging.domain.entity.User;
import com.example.digging.domain.network.LoginDto;
import com.example.digging.domain.network.TokenDto;
import com.example.digging.domain.network.exception.DuplicateMemberException;
import com.example.digging.domain.network.UserDto;
import com.example.digging.domain.network.response.UserApiResponse;
import com.example.digging.domain.repository.RefreshTokenRepository;
import com.example.digging.domain.repository.UserRepository;
import com.example.digging.util.SecurityUtil;

import org.hibernate.Session;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    private final TokenProvider tokenProvider;
    private final AuthenticationManagerBuilder authenticationManagerBuilder;

    private final RefreshTokenRepository refreshTokenRepository;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder, TokenProvider tokenProvider, AuthenticationManagerBuilder authenticationManagerBuilder, RefreshTokenRepository refreshTokenRepository) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.tokenProvider = tokenProvider;
        this.authenticationManagerBuilder = authenticationManagerBuilder;
        this.refreshTokenRepository = refreshTokenRepository;
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

    @Transactional
    public TokenDto login(LoginDto loginDto) {
        // 1. Login ID/PW 를 기반으로 AuthenticationToken 생성
        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(loginDto.getUsername(), loginDto.getPassword());

        // 2. 실제로 검증 (사용자 비밀번호 체크) 이 이루어지는 부분
        //    authenticate 메서드가 실행이 될 때 CustomUserDetailsService 에서 만들었던 loadUserByUsername 메서드가 실행됨
        Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);

        // 3. 인증 정보를 기반으로 JWT 토큰 생성
        TokenDto jwt = tokenProvider.createToken(authentication);

         // 4. RefreshToken 저장
        RefreshToken savetoken = RefreshToken.builder()
                .token(jwt.getRefreshToken())
                .createdAt(LocalDateTime.now())
                .updatedAt(jwt.getAccessTokenExpiresIn())
                .username(authentication.getName())
                .build();

        refreshTokenRepository.save(savetoken);

        // 5. 토큰 발급
        return jwt;
    }

    @Transactional
    public TokenDto reissue(String access, String refresh) {
        // 1. Refresh Token 검증
        if (!tokenProvider.validateToken(refresh)) {
            throw new RuntimeException("Refresh Token 이 유효하지 않습니다.");
        }

        // 2. Access Token 에서 UserName 가져오기
        Authentication authentication = tokenProvider.getAuthentication(access);

        // 3. 저장소에서 Member ID 를 기반으로 Refresh Token 값 가져옴
        RefreshToken refreshToken = refreshTokenRepository.findById(authentication.getName())
                .orElseThrow(() -> new RuntimeException("로그아웃 된 사용자입니다."));

        // 4. Refresh Token 일치하는지 검사
        if (!refreshToken.getToken().equals(refresh)) {
            throw new RuntimeException("토큰의 유저 정보가 일치하지 않습니다.");
        }

        // 5. 새로운 토큰 생성
        TokenDto tokenDto = tokenProvider.createToken(authentication);

        // 6. 저장소 정보 업데이트
        RefreshToken newRefreshToken = refreshToken.updateValue(tokenDto.getRefreshToken(), LocalDateTime.now(), tokenDto.getAccessTokenExpiresIn());
        refreshTokenRepository.save(newRefreshToken);

        // 토큰 발급
        return tokenDto;
    }


    @Transactional(readOnly = true)
    public UserDto getUserInfoWithAutorities() {

        Optional<User> userInfo = SecurityUtil.getCurrentUsername().flatMap(userRepository::findOneWithAuthoritiesByUsername);

        RefreshToken refreshToken = refreshTokenRepository.findById(userInfo.get().getUsername())
                .orElseThrow(() -> new RuntimeException("로그아웃 된 사용자입니다."));

        return userInfo
                .map(user -> {
                    UserDto userDto = UserDto.builder()
                            .email(user.getEmail())
                            .interest(user.getInterest())
                            .userId(user.getUserId())
                            .oauthId(user.getOauthId())
                            .username(user.getUsername())
                            .provider(user.getProvider())
                            .createdAt(user.getCreatedAt())
                            .updatedAt(user.getUpdatedAt())
                            .activated(user.getActivated())
                            .refreshTokenCreatedAt(refreshToken.getCreatedAt())
                            .refreshTokenUpdatedAt(refreshToken.getUpdatedAt())
                            .build();
                    return userDto;
                })
                .orElseThrow();
    }

    @Transactional(readOnly = true)
    public Optional<User> getMyUserWithAuthorities() {
        return SecurityUtil.getCurrentUsername().flatMap(userRepository::findOneWithAuthoritiesByUsername);
    }
}