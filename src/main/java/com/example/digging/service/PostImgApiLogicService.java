package com.example.digging.service;

import com.example.digging.domain.entity.*;
import com.example.digging.domain.network.request.PostImgApiRequest;
import com.example.digging.domain.network.request.PostLinkApiRequest;
import com.example.digging.domain.network.response.ImgsApiResponse;
import com.example.digging.domain.network.response.PostImgApiResponse;
import com.example.digging.domain.network.response.PostTextApiResponse;
import com.example.digging.domain.repository.*;
import com.example.digging.ifs.CrudInterface;
import com.example.digging.util.SecurityUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class PostImgApiLogicService implements CrudInterface<PostImgApiRequest, PostImgApiResponse> {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PostsRepository postsRepository;

    @Autowired
    private UserHasPostsRepository userHasPostsRepository;

    @Autowired
    private PostImgRepository postImgRepository;

    @Autowired
    private ImgsRepository imgsRepository;

    @Autowired
    private TagsRepository tagsRepository;

    @Autowired
    private PostTagRepository postTagRepository;

    @Override
    public PostImgApiResponse create(PostImgApiRequest request) {
        return null;
    }

    public PostImgApiResponse create(String title, List<String> tagsList, ArrayList<String> imageUrls){

        User userInfo = SecurityUtil.getCurrentUsername().flatMap(userRepository::findOneWithAuthoritiesByUsername)
                .orElseThrow(() -> new RuntimeException("token 오류 입니다. 사용자를 찾을 수 없습니다."));

        List<String> tags = tagsList;

        ArrayList<String> newTagList = new ArrayList<String>();

        Posts posts = Posts.builder()
                .isText(Boolean.FALSE)
                .isImg(Boolean.TRUE)
                .isLink(Boolean.FALSE)
                .isLike(Boolean.FALSE)
                .createdAt(LocalDateTime.now())
                .createdBy(userInfo.getUsername())
                .updatedAt(LocalDateTime.now())
                .updatedBy(userInfo.getUsername())
                .build();
        Posts newPosts = postsRepository.save(posts);

        for (int i=0; i<tags.size(); i++) {
            String checkTag = tags.get(i);
            Tags existTag = tagsRepository.findByTagsAndUser_UserId(checkTag, userInfo.getUserId());
            if (existTag != null){
                PostTag postTag = PostTag.builder()
                        .posts(newPosts)
                        .tags(existTag)
                        .build();
                postTagRepository.save(postTag);
            } else {

                Tags newTags = Tags.builder()
                        .tags(tags.get(i))
                        .user(userRepository.getOne(userInfo.getUserId()))
                        .build();
                Tags newTag = tagsRepository.save(newTags);
                newTagList.add(tags.get(i));
                PostTag postTag = PostTag.builder()
                        .posts(newPosts)
                        .tags(newTag)
                        .build();

                postTagRepository.save(postTag);
            }
        }

        UserHasPosts userHasPosts = UserHasPosts.builder()
                .user(userRepository.getOne(userInfo.getUserId()))
                .posts(newPosts)
                .build();
        userHasPostsRepository.save(userHasPosts);

        PostImg postImg = PostImg.builder()
                .posts(newPosts)
                .title(title)
                .createdAt(LocalDateTime.now())
                .createdBy(userInfo.getUsername())
                .updatedAt(LocalDateTime.now())
                .updatedBy(userInfo.getUsername())
                .build();

        PostImg newPostImg = postImgRepository.save(postImg);

        ArrayList<ImgsApiResponse> imgsResponse = new ArrayList<>();

        for (int i=0; i<imageUrls.size(); i++){
            Imgs imgs = Imgs.builder()
                    .postImg(newPostImg)
                    .imgUrl(imageUrls.get(i))
                    .build();
            imgsRepository.save(imgs);

            ImgsApiResponse imgsApiResponse = ImgsApiResponse.builder()
                    .id(imgs.getId())
                    .imgUrl(imgs.getImgUrl())
                    .build();
            imgsResponse.add(imgsApiResponse);
        }

        return response(newPostImg, newTagList, imgsResponse);
    }

    @Override
    public PostImgApiResponse read(Integer id) {
        return null;
    }

    @Override
    public PostImgApiResponse update(Integer id, PostImgApiRequest request) {
        return null;
    }

    @Override
    public PostImgApiResponse delete(Integer id) {
        return null;
    }

    private PostImgApiResponse response(PostImg postImg, ArrayList<String> tags, ArrayList<ImgsApiResponse> imgsResponse){

        PostImgApiResponse postImgApiResponse = PostImgApiResponse.builder()
                .resultCode("Success")
                .type("text")
                .userName(postImg.getCreatedBy())
                .postId(postImg.getPosts().getPostId())
                .imgId(postImg.getImgId())
                .title(postImg.getTitle())
                .newTags(tags)
                .imgs(imgsResponse)
                .build();

        return postImgApiResponse;

    }
}
