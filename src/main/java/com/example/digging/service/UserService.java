package com.example.digging.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.regex.Pattern;

import com.example.digging.adapter.apple.AppleServiceImpl;
import com.example.digging.adapter.google.GoogleServiceImpl;
import com.example.digging.adapter.jwt.TokenProvider;
import com.example.digging.domain.entity.*;
import com.example.digging.domain.network.TokenDto;

import com.example.digging.domain.network.UserDto;
import com.example.digging.domain.network.request.LoginRequest;
import com.example.digging.domain.network.request.UpdateUserRequest;
import com.example.digging.domain.network.response.*;
import com.example.digging.domain.repository.*;
import com.example.digging.util.SecurityUtil;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
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
    private final AppleServiceImpl appleImpl;
    private final GoogleServiceImpl googleImpl;

    @Autowired
    private TagsRepository tagsRepository;

    @Autowired
    private PostsRepository postsRepository;

    @Autowired
    private PostTagRepository postTagRepository;

    @Autowired
    private PostTextRepository postTextRepository;

    @Autowired
    private PostImgRepository postImgRepository;

    @Autowired
    private PostLinkRepository postLinkRepository;

    @Autowired
    private UserHasPostsRepository userHasPostsRepository;



    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder, TokenProvider tokenProvider, AuthenticationManagerBuilder authenticationManagerBuilder, RefreshTokenRepository refreshTokenRepository, AppleServiceImpl appleService, GoogleServiceImpl googleService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.tokenProvider = tokenProvider;
        this.authenticationManagerBuilder = authenticationManagerBuilder;
        this.refreshTokenRepository = refreshTokenRepository;
        this.appleImpl = appleService;
        this.googleImpl = googleService;
    }

    @Transactional
    public ResponseEntity<TokenDto> signup(String uid, String email, String provider) {

        if (userRepository.findOneWithAuthoritiesByUid(uid).orElse(null) != null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new ErrorResponse("?????? ???????????? ?????? ???????????????", "401"));
        }

        //?????? ????????? ??????
        Authority authority = Authority.builder()
                .authorityName("ROLE_USER")
                .build();


        User user = User.builder()
                .username("????????????")
                .email(email)
                .password(passwordEncoder.encode(makePwd(provider, uid)))
                .provider(provider)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .uid(uid)
                .authorities(Collections.singleton(authority))
                .activated(true)
                .build();

        userRepository.save(user);

        return ResponseEntity.ok(login(uid, provider));
    }

    public String makePwd(String provider, String uid) {
        if(provider.equals("apple")) {
            return "${apple.secret}" + uid;
        } else {
            return "${google.secret}" + uid;
        }
    }

    public String makeUid(String provider, String token) {
        if(provider.equals("apple")) {
            String uid = appleImpl.getAppleSUBIdentity(token);
            return uid;
        } else {
            String uid = googleImpl.verifyAndGetUid(token);
            return uid;
        }
    }

    @Transactional
    public ResponseEntity<TokenDto> login(LoginRequest request) {
        LoginRequest body = request;

        String uid = makeUid(body.getProvider(), body.getToken());
        String pwd = makePwd(body.getProvider(), uid);

        if (uid == "not valid id_token") {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ErrorResponse("token ?????? ?????????. token ?????? ???????????? ????????????", "404"));

        }
        if (uid == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ErrorResponse("token ?????? ?????????. token ?????? ???????????? ????????????", "404"));
        }

        // 1. Login ID/PW ??? ???????????? AuthenticationToken ??????
        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(uid, pwd);

        // 2. ????????? ?????? (????????? ???????????? ??????) ??? ??????????????? ??????
        //    authenticate ???????????? ????????? ??? ??? CustomUserDetailsService ?????? ???????????? loadUserByUsername ???????????? ?????????
        Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);

        // 3. ?????? ????????? ???????????? JWT ?????? ??????
        TokenDto jwt = tokenProvider.createToken(authentication);

        System.out.println(authentication);
        Integer userId = userRepository.findByUid(authentication.getName()).get().getUserId();

         // 4. RefreshToken ??????
        RefreshToken savetoken = RefreshToken.builder()
                .token(jwt.getRefreshToken())
                .createdAt(LocalDateTime.now())
                .updatedAt(jwt.getAccessTokenExpiresIn())
                .userId(userId)
                .build();

        refreshTokenRepository.save(savetoken);

        // 5. ?????? ??????
        return ResponseEntity.ok(jwt);
    }

    @Transactional
    public TokenDto login(String uid, String provider) {

        String pwd = makePwd(provider, uid);

        // 1. Login ID/PW ??? ???????????? AuthenticationToken ??????
        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(uid, pwd);

        // 2. ????????? ?????? (????????? ???????????? ??????) ??? ??????????????? ??????
        //    authenticate ???????????? ????????? ??? ??? CustomUserDetailsService ?????? ???????????? loadUserByUsername ???????????? ?????????
        Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);

        // 3. ?????? ????????? ???????????? JWT ?????? ??????
        TokenDto jwt = tokenProvider.createToken(authentication);

        System.out.println(authentication);
        Integer userId = userRepository.findByUid(authentication.getName()).get().getUserId();

        // 4. RefreshToken ??????
        RefreshToken savetoken = RefreshToken.builder()
                .token(jwt.getRefreshToken())
                .createdAt(LocalDateTime.now())
                .updatedAt(jwt.getAccessTokenExpiresIn())
                .userId(userId)
                .build();

        refreshTokenRepository.save(savetoken);

        // 5. ?????? ??????
        return jwt;
    }

    @Transactional
    public ResponseEntity<TokenDto> reissue(String access, String refresh) {
        // 1. Refresh Token ??????
        if (!tokenProvider.validateToken(refresh)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ErrorResponse("Refresh Token??? ???????????? ????????????", "404"));

        }

        // 2. Access Token ?????? UserName ????????????
        Authentication authentication = tokenProvider.getAuthentication(access);

        // 3. ??????????????? Member ID ??? ???????????? Refresh Token ??? ?????????
        RefreshToken refreshToken = refreshTokenRepository.findByUserId(userRepository.findByUid(authentication.getName()).get().getUserId())
                .orElseThrow(() -> new RuntimeException("???????????? ??? ??????????????????."));

        // 4. Refresh Token ??????????????? ??????
        if (!refreshToken.getToken().equals(refresh)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new ErrorResponse("????????? ?????? ????????? ???????????? ????????????.", "401"));
        }

        // 5. ????????? ?????? ??????
        TokenDto tokenDto = tokenProvider.createToken(authentication);

        // 6. ????????? ?????? ????????????
        RefreshToken newRefreshToken = refreshToken.updateValue(tokenDto.getRefreshToken(), LocalDateTime.now(), tokenDto.getAccessTokenExpiresIn());
        refreshTokenRepository.save(newRefreshToken);

        // ?????? ??????
        return ResponseEntity.ok(tokenDto);
    }

    @Transactional(readOnly = true)
    public UserDto getUserInfoWithAutorities() {

        Optional<User> userInfo = SecurityUtil.getCurrentUsername().flatMap(userRepository::findOneWithAuthoritiesByUid);

        RefreshToken refreshToken = refreshTokenRepository.findByUserId(userInfo.get().getUserId())
                .orElseThrow(() -> new RuntimeException("???????????? ??? ??????????????????."));



        return userInfo
                .map(user -> {
                    UserDto userDto = UserDto.builder()
                            .email(user.getEmail())
                            .interest(user.getInterest())
                            .userId(user.getUserId())
                            .uid(user.getUid())
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

    @Transactional
    public ResponseEntity<UserDto> updateUser(UpdateUserRequest updateUserRequest) {
        Optional<User> userInfo = SecurityUtil.getCurrentUsername().flatMap(userRepository::findOneWithAuthoritiesByUid);
        UpdateUserRequest body = updateUserRequest;

        Boolean emailCheck = isValidEmailAddress(body.getEmail());
        Boolean nicknameCheck = isValidNickname(body.getUsername());

        if (!emailCheck && !nicknameCheck) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new UpdateUserErrorResponse("???????????? ??????, ?????? ?????????, ?????? ??? 5??? ????????? ??????????????????", "????????? ????????? ???????????? ??????????????????"));
        }
        if (!nicknameCheck && emailCheck) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new UpdateUserErrorResponse(Boolean.TRUE, Boolean.FALSE, "???????????? ??????, ?????? ?????????, ?????? ??? 5??? ????????? ??????????????????"));
        }

        if (nicknameCheck && !emailCheck) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new UpdateUserErrorResponse(Boolean.FALSE, Boolean.TRUE, "????????? ????????? ???????????? ??????????????????"));
        }

        return userInfo.map(user -> {
            user
                    .setUpdatedAt(LocalDateTime.now())
                    .setUsername(body.getUsername())
                    .setEmail(body.getEmail())
                    .setInterest(body.getInterest())
            ;
            return user;
        })
                .map(user -> userRepository.save(user))
                .map(user -> {
                    UserDto userDto = UserDto.builder()
                            .email(user.getEmail())
                            .interest(user.getInterest())
                            .userId(user.getUserId())
                            .uid(user.getUid())
                            .username(user.getUsername())
                            .provider(user.getProvider())
                            .createdAt(user.getCreatedAt())
                            .updatedAt(user.getUpdatedAt())
                            .activated(user.getActivated())
                            .refreshTokenCreatedAt(refreshTokenRepository.findByUserId(user.getUserId()).get().getCreatedAt())
                            .refreshTokenUpdatedAt(refreshTokenRepository.findByUserId(user.getUserId()).get().getUpdatedAt())
                            .build();
                    return ResponseEntity.ok(userDto);

                })
                .orElseThrow();
    }

    public PostsResponse deletePost(Integer postid) {
        User userInfo = SecurityUtil.getCurrentUsername().flatMap(userRepository::findOneWithAuthoritiesByUid)
                .orElseThrow(() -> new RuntimeException("token ?????? ?????????. ???????????? ?????? ??? ????????????."));

        Optional<UserHasPosts> optional = userHasPostsRepository.findByUser_UserIdAndPostsPostId(userInfo.getUserId(), postid);

        return optional
                .map(opt -> {
                    Posts posts = opt.getPosts();

                    List<PostTag> postTagList = posts.getPostTagList();
                    postsRepository.delete(posts);
                    userHasPostsRepository.delete(opt);

                    for (int i =0; i<postTagList.size(); i++) {
                        postTagRepository.delete(postTagList.get(i));
                        if(postTagList.get(i).getTags().getPostTagList().isEmpty()){
                            tagsRepository.delete(postTagList.get(i).getTags());
                        }

                    }


                    PostsResponse postsResponse = PostsResponse.builder()
                            .resultCode("Delete Success")
                            .build();
                    return postsResponse;


                })
                .orElseGet(
                        ()->{
                            PostsResponse erros = PostsResponse.builder()
                                    .resultCode("Error : ????????? ??????")
                                    .build();
                            return erros;
                        }
                );


    }

    public TotalTagResponse getUserTotalTags() {
        User userInfo = SecurityUtil.getCurrentUsername().flatMap(userRepository::findOneWithAuthoritiesByUid)
                .orElseThrow(() -> new RuntimeException("token ?????? ?????????. ???????????? ?????? ??? ????????????."));

        List<Tags> userTagList = tagsRepository.findAllByUser_UserId(userInfo.getUserId());
        int userTagNum = userTagList.size();
        ArrayList<String> tagStr = new ArrayList<String>();

        for (int i =0;i<userTagNum;i++) {
            tagStr.add(userTagList.get(i).getTags());
        }

        if (userTagNum>0){
            TotalTagResponse totalTagResponse = TotalTagResponse.builder()
                    .resultCode("Success")
                    .totalNum(userTagNum)
                    .totalTags(tagStr)
                    .build();
            return totalTagResponse;
        }else{
            TotalTagResponse totalTagResponse = TotalTagResponse.builder()
                    .resultCode("????????? ??????")
                    .build();
            return totalTagResponse;
        }


    }

    public PostsResponse setLike(Integer postid) {
        User userInfo = SecurityUtil.getCurrentUsername().flatMap(userRepository::findOneWithAuthoritiesByUid)
                .orElseThrow(() -> new RuntimeException("token ?????? ?????????. ???????????? ?????? ??? ????????????."));

        Optional<UserHasPosts> optional = userHasPostsRepository.findByUser_UserIdAndPostsPostId(userInfo.getUserId(), postid);

        return optional
                .map(opt -> {
                    Posts posts = opt.getPosts();
                    posts.setIsLike(!posts.getIsLike())
                            .setUpdatedAt(LocalDateTime.now())
                    ;
                    if(posts.getIsText()==Boolean.TRUE){
                        PostText postText = postTextRepository.findByPostsPostId(opt.getPosts().getPostId());
                        postTextRepository.save(postText.setUpdatedAt(LocalDateTime.now()));
                    }

                    if(posts.getIsLink()==Boolean.TRUE){
                        PostLink postLink = postLinkRepository.findByPostsPostId(opt.getPosts().getPostId());
                        postLinkRepository.save(postLink.setUpdatedAt(LocalDateTime.now()));
                    }

                    if(posts.getIsImg()==Boolean.TRUE){
                        PostImg postImg = postImgRepository.findByPostsPostId(opt.getPosts().getPostId());
                        postImgRepository.save(postImg.setUpdatedAt(LocalDateTime.now()));
                    }
                    return posts;
                })
                .map(posts -> postsRepository.save(posts))
                .map(updatePost -> postres(updatePost))
                .orElseGet(
                        ()->{
                            PostsResponse errres = PostsResponse.builder()
                                    .resultCode("????????? ??????")
                                    .build();
                            return errres;
                        }
                );
    }

    public GetPostNumByTypeResponse getPostNumByType() {

        User userInfo = SecurityUtil.getCurrentUsername().flatMap(userRepository::findOneWithAuthoritiesByUid)
                .orElseThrow(() -> new RuntimeException("token ?????? ?????????. ???????????? ?????? ??? ????????????."));

        List<UserHasPosts> userHasPostsList = userHasPostsRepository.findAllByUser_UserId(userInfo.getUserId());
        int postsNum = userHasPostsList.size();
        Integer textNum = 0; Integer imgNum = 0; Integer linkNum = 0;
        for(int i=0;i<postsNum;i++){
            if (userHasPostsList.get(i).getPosts().getIsText() == Boolean.TRUE) {textNum += 1;}
            if (userHasPostsList.get(i).getPosts().getIsImg() == Boolean.TRUE) {imgNum += 1;}
            if (userHasPostsList.get(i).getPosts().getIsLink() == Boolean.TRUE) {linkNum += 1;}
        }

        return numres(textNum, imgNum, linkNum, userInfo.getUserId());
    }

    private GetPostNumByTypeResponse numres(Integer text, Integer img, Integer link, Integer userid) {
        Optional<User> user = userRepository.findById(userid);
        String username = user.map(finduser -> finduser.getUsername()).orElseGet(()->"User Error");

        GetPostNumByTypeResponse getPostNumByTypeResponse = GetPostNumByTypeResponse.builder()
                .resultCode("Success")
                .userName(username)
                .totalText(text)
                .totalImg(img)
                .totalLink(link)
                .build();

        return getPostNumByTypeResponse;
    }


    private PostsResponse postres(Posts posts) {
        PostsResponse postsResponse = PostsResponse.builder()
                .resultCode("Success")
                .postId(posts.getPostId())
                .isText(posts.getIsText())
                .isImg(posts.getIsImg())
                .isLink(posts.getIsLink())
                .isLike(posts.getIsLike())
                .createdAt(posts.getCreatedAt())
                .createdBy(posts.getCreatedBy())
                .updatedAt(posts.getUpdatedAt())
                .updatedBy(posts.getUpdatedBy())
                .build();
        String typeStr = null;
        if (postsResponse.getIsText() == Boolean.TRUE) {
            typeStr = "text";
        }
        if (postsResponse.getIsImg() == Boolean.TRUE) {
            typeStr = "img";
        }
        if (postsResponse.getIsLink() == Boolean.TRUE) {
            typeStr = "link";
        }
        postsResponse.setType(typeStr);
        return postsResponse;
    }

    public boolean isValidEmailAddress(String email) {
        String ePattern = "^[a-zA-Z0-9.!#$%&'*+/=?^_`{|}~-]+@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\])|(([a-zA-Z\\-0-9]+\\.)+[a-zA-Z]{2,}))$";
        java.util.regex.Pattern p = java.util.regex.Pattern.compile(ePattern);
        java.util.regex.Matcher m = p.matcher(email);
        return m.matches();
    }

    public boolean isValidNickname(String username) {
        if (username.length() > 5 || username.length() < 1) {
            return Boolean.FALSE;
        } else {
            for (int i=0;i<username.length();i++) {
                boolean a = Pattern.matches("^[a-z]*$", Character.toString(username.charAt(i)));
                boolean b = Pattern.matches("^[0-9]*$", Character.toString(username.charAt(i)));
                boolean c = Pattern.matches("^[???-??????-???]*$", Character.toString(username.charAt(i)));
                if(!a && !b && !c) {
                    return Boolean.FALSE;
                }
            }
            return Boolean.TRUE;
        }

    }

    @Transactional(readOnly = true)
    public Optional<User> getMyUserWithAuthorities() {
        return SecurityUtil.getCurrentUsername().flatMap(userRepository::findOneWithAuthoritiesByUid);
    }
}