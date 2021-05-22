package com.example.digging.service;

import com.example.digging.domain.entity.*;
import com.example.digging.domain.network.Header;
import com.example.digging.domain.network.request.PostLinkApiRequest;
import com.example.digging.domain.network.response.PostLinkApiResponse;
import com.example.digging.domain.repository.*;
import com.example.digging.ifs.CrudInterface;
import com.example.digging.util.UrlTypeValidation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.support.NullValue;
import org.springframework.stereotype.Service;


import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

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

    @Autowired
    private TagsRepository tagsRepository;

    @Autowired
    private PostTagRepository postTagRepository;

//    for (int i=0; i<weeks.length; i++) {
//        System.out.println(weeks[i]);
//    }

    @Override
    public Header<PostLinkApiResponse> create(Header<PostLinkApiRequest> request) {
        PostLinkApiRequest body = request.getData();
        String linkStirng = body.getUrl();

        String[] tags = body.getTagsArr();
        ArrayList<String> newTagList = new ArrayList<String>();
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

            for (int i=0; i<tags.length; i++) {
                String checkTag = tags[i];
                Tags existTag = tagsRepository.findByTagsAndUserId(checkTag, body.getUserId());
                if (existTag != null){
                    PostTag postTag = PostTag.builder()
                            .posts(newPosts)
                            .tags(existTag)
                            .build();
                    postTagRepository.save(postTag);
                } else {

                    Tags newTags = Tags.builder()
                            .tags(tags[i])
                            .user(userRepository.getOne(body.getUserId()))
                            .build();
                    Tags newTag = tagsRepository.save(newTags);
                    newTagList.add(tags[i]);
                    PostTag postTag = PostTag.builder()
                            .posts(newPosts)
                            .tags(newTag)
                            .build();

                    postTagRepository.save(postTag);
                }
            }

            UserHasPosts userHasPosts = UserHasPosts.builder()
                    .user(userRepository.getOne(body.getUserId()))
                    .posts(newPosts)
                    .build();
            userHasPostsRepository.save(userHasPosts);

            PostLink postLink = PostLink.builder()
                    .posts(newPosts)
                    .title(body.getTitle())
                    .url(body.getUrl())
                    .createdAt(LocalDateTime.now())
                    .createdBy(body.getUserName())
                    .updatedAt(LocalDateTime.now())
                    .updatedBy(body.getUserName())
                    .build();
            PostLink newPostLink = postLinkRepository.save(postLink);

            return response(newPostLink, "Valid Url", newTagList);


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

    private Header<PostLinkApiResponse> response(PostLink postLink, String valid, ArrayList<String> tags){

        PostLinkApiResponse postLinkApiResponse = PostLinkApiResponse.builder()
                .userName(postLink.getCreatedBy())
                .postId(postLink.getPosts().getPostId())
                .linkId(postLink.getLinkId())
                .title(postLink.getTitle())
                .urlCheck(valid)
                .newTags(tags)
                .build();

        return Header.OK(postLinkApiResponse);

    }
}
