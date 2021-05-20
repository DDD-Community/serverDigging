package com.example.digging.service;

import com.example.digging.domain.entity.UserHasPosts;
import com.example.digging.domain.network.Header;
import com.example.digging.domain.network.request.UserHasPostsApiRequest;
import com.example.digging.domain.network.response.UserHasPostsApiResponse;
import com.example.digging.domain.repository.PostsRepository;
import com.example.digging.domain.repository.UserHasPostsRepository;
import com.example.digging.domain.repository.UserRepository;
import com.example.digging.ifs.CrudInterface;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserHasPostsApiLogicService implements CrudInterface<UserHasPostsApiRequest, UserHasPostsApiResponse> {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PostsRepository postsRepository;

    @Autowired
    private UserHasPostsRepository userHasPostsRepository;

    @Override
    public Header<UserHasPostsApiResponse> create(Header<UserHasPostsApiRequest> request) {
        UserHasPostsApiRequest body = request.getData();

        UserHasPosts userHasPosts = UserHasPosts.builder()
                .user(userRepository.getOne(body.getUserId()))
                .posts(postsRepository.getOne(body.getPostsPostId()))
                .build();
        UserHasPosts newUserHasPosts = userHasPostsRepository.save(userHasPosts);
        return response(newUserHasPosts);
    }

    @Override
    public Header<UserHasPostsApiResponse> read(Integer id) {
        return null;
    }

    @Override
    public Header<UserHasPostsApiResponse> update(Integer id, Header<UserHasPostsApiRequest> request) {
        return null;
    }

    @Override
    public Header delete(Integer id) {
        return null;
    }

    private Header<UserHasPostsApiResponse> response(UserHasPosts userHasPosts){
        UserHasPostsApiResponse body = UserHasPostsApiResponse.builder()
                .id(userHasPosts.getId())
                .userId(userHasPosts.getUser().getId())
                .postsPostId(userHasPosts.getPosts().getPostId())
                .build();
        return Header.OK(body);
    }
}
