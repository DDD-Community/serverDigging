package com.example.digging.service;

import com.example.digging.domain.entity.PostLink;
import com.example.digging.domain.entity.Posts;
import com.example.digging.domain.entity.UserHasPosts;
import com.example.digging.domain.network.Header;
import com.example.digging.domain.network.request.PostLinkApiRequest;
import com.example.digging.domain.network.response.PostLinkApiResponse;
import com.example.digging.domain.repository.PostLinkRepository;
import com.example.digging.domain.repository.PostsRepository;
import com.example.digging.domain.repository.UserHasPostsRepository;
import com.example.digging.domain.repository.UserRepository;
import com.example.digging.ifs.CrudInterface;
import com.example.digging.util.UrlTypeValidation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import java.time.LocalDateTime;

@Service
public class PostLinkApiLogicService implements CrudInterface<PostLinkApiRequest, PostLinkApiResponse> {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PostsRepository postsRepository;

    @Autowired
    private UserHasPostsRepository userHasPostsRepository;

    @Autowired
    private PostLinkRepository postLinkRepository;

    @Autowired
    private UrlTypeValidation urlTypeValidation;

    @Override
    public Header<PostLinkApiResponse> create(Header<PostLinkApiRequest> request) {
        PostLinkApiRequest body = request.getData();
        String linkStirng = body.getUrl();
        Boolean urlCheck = urlTypeValidation.valid(linkStirng);

        if(urlCheck == Boolean.TRUE){
            Posts posts = Posts.builder()
                    .isText(Boolean.FALSE)
                    .isImg(Boolean.FALSE)
                    .isLink(Boolean.TRUE)
                    .isLike(Boolean.FALSE)
                    .createdAt(LocalDateTime.now())
                    .createdBy(body.getUserName())
                    .updatedAt(LocalDateTime.now())
                    .updatedBy(body.getUserName())
                    .build();
            Posts newPosts = postsRepository.save(posts);

            UserHasPosts userHasPosts = UserHasPosts.builder()
                    .user(userRepository.getOne(body.getUserId()))
                    .posts(postsRepository.getOne(posts.getPostId()))
                    .build();
            UserHasPosts newUserHasPosts = userHasPostsRepository.save(userHasPosts);

            PostLink postLink = PostLink.builder()
                    .posts(postsRepository.getOne(posts.getPostId()))
                    .title(body.getTitle())
                    .url(body.getUrl())
                    .createdAt(LocalDateTime.now())
                    .createdBy(body.getUserName())
                    .updatedAt(LocalDateTime.now())
                    .updatedBy(body.getUserName())
                    .build();
            PostLink newPostLink = postLinkRepository.save(postLink);

            return response(newPostLink, "Valid Url");


        }else{
            return Header.ERROR("링크 주소 유효하지 않음");
        }
    }

    @Override
    public Header<PostLinkApiResponse> read(Integer id) {
        return null;
    }

    @Override
    public Header<PostLinkApiResponse> update(Integer id, Header<PostLinkApiRequest> request) {
        return null;
    }

    @Override
    public Header delete(Integer id) {
        return null;
    }

    private Header<PostLinkApiResponse> response(PostLink postLink, String valid){

        PostLinkApiResponse postLinkApiResponse = PostLinkApiResponse.builder()
                .userName(postLink.getCreatedBy())
                .postId(postLink.getPosts().getPostId())
                .linkId(postLink.getLinkId())
                .title(postLink.getTitle())
                .urlCheck(valid)
                .build();

        return Header.OK(postLinkApiResponse);

    }
}
