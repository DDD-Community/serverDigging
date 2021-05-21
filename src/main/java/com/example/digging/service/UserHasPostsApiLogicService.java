package com.example.digging.service;

import com.example.digging.domain.entity.User;
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

import java.util.Optional;

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
        Optional<UserHasPosts> optional = userHasPostsRepository.findById(id);
        return optional
                .map(userHasPosts -> response(userHasPosts))
                .orElseGet(
                        ()->Header.ERROR("데이터 없음")
                );
    }

    @Override
    public Header<UserHasPostsApiResponse> update(Integer id, Header<UserHasPostsApiRequest> request) {
        return null;
    }

    @Override
    public Header delete(Integer id) {
        Optional<UserHasPosts> optional = userHasPostsRepository.findById(id);
        return optional
                .map(userHasPosts -> {
                    userHasPostsRepository.delete(userHasPosts);
                    return Header.OK();
                })
                .orElseGet(
                        ()->Header.ERROR("데이터 없음")
                );
    }

    private Header<UserHasPostsApiResponse> response(UserHasPosts userHasPosts){
        String typeCheck = "";
        if (userHasPosts.getPosts().getIsImg() == Boolean.TRUE) {
            typeCheck = "Img";
        }else {
            if(userHasPosts.getPosts().getIsText() == Boolean.TRUE) {
                typeCheck = "Text";
            } else {
                typeCheck = "Link";
            }
        }

        UserHasPostsApiResponse body = UserHasPostsApiResponse.builder()
                .id(userHasPosts.getId())
                .userId(userHasPosts.getUser().getId())
                .userName(userHasPosts.getUser().getUsername())
                .postsPostId(userHasPosts.getPosts().getPostId())
                .postsType(typeCheck)
                .build();
        return Header.OK(body);
    }
}
