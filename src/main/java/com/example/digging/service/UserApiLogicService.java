package com.example.digging.service;

import com.example.digging.domain.entity.*;
import com.example.digging.domain.network.request.UserApiRequest;
import com.example.digging.domain.network.response.*;
import com.example.digging.domain.repository.*;
import com.example.digging.ifs.CrudInterface;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class UserApiLogicService implements CrudInterface<UserApiRequest, UserApiResponse> {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TagsRepository tagsRepository;

    @Autowired
    private PostsRepository postsRepository;

    @Autowired
    private PostTagRepository postTagRepository;

    @Autowired
    private PostTextRepository postTextRepository;

    @Autowired
    private PostLinkRepository postLinkRepository;

    @Autowired
    private UserHasPostsRepository userHasPostsRepository;

    @Override
    public UserApiResponse create(UserApiRequest request) {
        UserApiRequest userApiRequest = request;

        User user = User.builder()
                .username(userApiRequest.getUsername())
                .email(userApiRequest.getEmail())
                .password(userApiRequest.getPassword())
                .provider(userApiRequest.getProvider())
                .interest(userApiRequest.getInterest())
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        User newUser = userRepository.save(user);

        return response(newUser);
    }

    @Override
    public UserApiResponse read(Integer id) {

        Optional<User> optional = userRepository.findById(id);
        System.out.println(optional);
        return optional
                .map(user -> response(user))
                .orElseGet(
                        ()->{UserApiResponse userApiResponse = UserApiResponse.builder()
                                .resultCode("Error : 데이터 없음")
                                .build();
                            return userApiResponse;}
                );
    }

    @Override
    public UserApiResponse update(Integer id, UserApiRequest request) {
        UserApiRequest userApiRequest = request;
        Optional<User> optional = userRepository.findById(id);
        System.out.println(userApiRequest);
        System.out.println(optional);

        return optional
                .map(user -> {
                      user
//                          .setAuthority("ROLE_GUEST")
                            .setUpdatedAt(LocalDateTime.now())
                            ;
                    return user;
                })
                .map(user -> userRepository.save(user))
                .map(updateUser -> response(updateUser))
                .orElseGet(
                        ()->{UserApiResponse userApiResponse = UserApiResponse.builder()
                                .resultCode("Error : 데이터 없음")
                                .build();
                            return userApiResponse;}
                );

    }

    @Override
    public UserApiResponse delete(Integer id) {

        System.out.println(id);
        Optional<User> optional = userRepository.findById(id);
        return optional
                .map(user -> {
                    userRepository.delete(user);
                    UserApiResponse userApiResponse = UserApiResponse.builder()
                            .resultCode("Delete Success")
                            .build();
                    return userApiResponse;
                })
                .orElseGet(()->{UserApiResponse userApiResponse = UserApiResponse.builder()
                        .resultCode("Error : 데이터 없음")
                        .build();
                    return userApiResponse;}
                );

    }


    private UserApiResponse response(User user){
        UserApiResponse userApiResponse = UserApiResponse.builder()
                .resultCode("Success")
                .id(user.getUserId())
                .username(user.getUsername())
                .email(user.getEmail())
                .password(user.getPassword())
                .provider(user.getProvider())
//                .role(String.valueOf(user.getAuthority()))
                .interest(user.getInterest())
                .createdAt(user.getCreatedAt())
                .updatedAt(user.getUpdatedAt())
                .build();

        return userApiResponse;

    }




}
