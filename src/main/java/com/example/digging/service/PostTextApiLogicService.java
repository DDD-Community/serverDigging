package com.example.digging.service;

import com.example.digging.domain.entity.*;
import com.example.digging.domain.network.Header;
import com.example.digging.domain.network.exception.DuplicateMemberException;
import com.example.digging.domain.network.request.PostLinkApiRequest;
import com.example.digging.domain.network.request.PostTextApiRequest;
import com.example.digging.domain.network.response.PostLinkApiResponse;
import com.example.digging.domain.network.response.PostLinkReadResponse;
import com.example.digging.domain.network.response.PostTextApiResponse;
import com.example.digging.domain.network.response.PostTextReadResponse;
import com.example.digging.domain.repository.*;
import com.example.digging.ifs.CrudInterface;
import com.example.digging.util.SecurityUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.lang.reflect.Array;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class PostTextApiLogicService implements CrudInterface<PostTextApiRequest, PostTextApiResponse> {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PostsRepository postsRepository;

    @Autowired
    private UserHasPostsRepository userHasPostsRepository;

    @Autowired
    private PostTextRepository postTextRepository;

    @Autowired
    private TagsRepository tagsRepository;

    @Autowired
    private PostTagRepository postTagRepository;


    @Override
    public PostTextApiResponse create(PostTextApiRequest request) {

        User userInfo = SecurityUtil.getCurrentUsername().flatMap(userRepository::findOneWithAuthoritiesByUid)
                .orElseThrow(() -> new RuntimeException("token 오류 입니다. 사용자를 찾을 수 없습니다."));

        PostTextApiRequest body = request;

        String[] tags = body.getTagsArr();
        ArrayList<String> newTagList = new ArrayList<String>();

        Posts posts = Posts.builder()
                .isText(Boolean.TRUE)
                .isImg(Boolean.FALSE)
                .isLink(Boolean.FALSE)
                .isLike(Boolean.FALSE)
                .createdAt(LocalDateTime.now())
                .createdBy(userInfo.getUsername())
                .updatedAt(LocalDateTime.now())
                .updatedBy(userInfo.getUsername())
                .build();
        Posts newPosts = postsRepository.save(posts);

        for (int i=0; i<tags.length; i++) {
            String checkTag = tags[i];
            Tags existTag = tagsRepository.findByTagsAndUser_UserId(checkTag, userInfo.getUserId());
            if (existTag != null){
                PostTag postTag = PostTag.builder()
                        .posts(newPosts)
                        .tags(existTag)
                        .build();
                postTagRepository.save(postTag);
            } else {

                Tags newTags = Tags.builder()
                        .tags(tags[i])
                        .user(userRepository.getOne(userInfo.getUserId()))
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
                .user(userRepository.getOne(userInfo.getUserId()))
                .posts(newPosts)
                .build();
        userHasPostsRepository.save(userHasPosts);

        PostText postText = PostText.builder()
                .posts(newPosts)
                .title(body.getTitle())
                .content(body.getContent())
                .createdAt(LocalDateTime.now())
                .createdBy(userInfo.getUsername())
                .updatedAt(LocalDateTime.now())
                .updatedBy(userInfo.getUsername())
                .build();

        PostText newPostText = postTextRepository.save(postText);

        return response(newPostText, newTagList);

    }

    @Override
    public PostTextApiResponse read(Integer id) {
        return null;
    }

    @Override
    public PostTextApiResponse update(Integer id, PostTextApiRequest request) {
        return null;
    }

    @Override
    public PostTextApiResponse delete(Integer id) {
        return null;
    }

    public PostTextReadResponse textread(Integer postid) {

        User userInfo = SecurityUtil.getCurrentUsername().flatMap(userRepository::findOneWithAuthoritiesByUid)
                .orElseThrow(() -> new RuntimeException("token 오류 입니다. 사용자를 찾을 수 없습니다."));

        Optional<UserHasPosts> optional = userHasPostsRepository.findByUser_UserIdAndPostsPostId(userInfo.getUserId(), postid);
        ArrayList<String> tagList = new ArrayList<String>();

        return optional
                .map(opt ->{
                    List<PostTag> tagopt = postTagRepository.findAllByPostsPostId(postid);
                    int tagNum = tagopt.size();
                    for(int i = 0;i<tagNum;i++){
                        tagList.add(tagopt.get(i).getTags().getTags());
                    }
                    Optional<PostText> textopt = Optional.ofNullable(postTextRepository.findByPostsPostId(postid));
                    return textopt
                            .map(posts -> {
                                PostText readPost = posts;
                                return readres(readPost, tagList);
                            })
                            .orElseGet(() -> {
                                PostTextReadResponse error = PostTextReadResponse.builder().resultCode("Error : post 정보 없음").build();
                                return error;
                            });
                })
                .orElseGet(()-> {
                    PostTextReadResponse error = PostTextReadResponse.builder().resultCode("Error : user id, post id 오류").build();
                    return error;
                });


    }

    public ArrayList<PostTextReadResponse> alltextread() {
        User userInfo = SecurityUtil.getCurrentUsername().flatMap(userRepository::findOneWithAuthoritiesByUid)
                .orElseThrow(() -> new RuntimeException("token 오류 입니다. 사용자를 찾을 수 없습니다."));

        Optional<User> optional = userRepository.findById(userInfo.getUserId());
        List<UserHasPosts> userHasPosts = userHasPostsRepository.findAllByUser_UserId(userInfo.getUserId());
        int userHasPostsNum = userHasPosts.size();

        ArrayList<PostText> postTexts = new ArrayList<PostText>();
        ArrayList<ArrayList> tags = new ArrayList();
        for(int i =0; i<userHasPostsNum; i++){
            if(userHasPosts.get(i).getPosts().getIsText() == Boolean.TRUE) {
                postTexts.add(postTextRepository.findByPostsPostId(userHasPosts.get(i).getPosts().getPostId()));
                List<PostTag> nowTags = postTagRepository.findAllByPostsPostId(userHasPosts.get(i).getPosts().getPostId());
                int nowTagsSize = nowTags.size();
                ArrayList<String> tagStr = new ArrayList<String>();
                for(int j=0;j<nowTagsSize;j++){
                    tagStr.add(nowTags.get(j).getTags().getTags());
                }
                tags.add(tagStr);
            }
        }
        System.out.println(tags);
        return optional.map(user -> allreadres(postTexts, tags)).orElseGet(()->{
            ArrayList<PostTextReadResponse> errorList = new ArrayList<PostTextReadResponse>();
            PostTextReadResponse error = PostTextReadResponse.builder().resultCode("Error").build();
            errorList.add(error);
            return errorList;
        });

    }

    private PostTextApiResponse response(PostText postText, ArrayList<String> tags){

        PostTextApiResponse postTextApiResponse = PostTextApiResponse.builder()
                .resultCode("Success")
                .type("text")
                .userName(postText.getCreatedBy())
                .postId(postText.getPosts().getPostId())
                .textId(postText.getTextId())
                .title(postText.getTitle())
                .content(postText.getContent())
                .newTags(tags)
                .build();

        return postTextApiResponse;

    }

    private ArrayList<PostTextReadResponse> allreadres(ArrayList<PostText> postText, ArrayList<ArrayList> tags){

        int postTextNum = postText.size();
        ArrayList<PostTextReadResponse> postTextReadResponsesList = new ArrayList<PostTextReadResponse>();

        for(int i=0; i<postTextNum;i++){
            PostTextReadResponse postTextReadResponse = PostTextReadResponse.builder()
                    .resultCode("Success")
                    .type("text")
                    .postId(postText.get(i).getPosts().getPostId())
                    .textId(postText.get(i).getTextId())
                    .title(postText.get(i).getTitle())
                    .content(postText.get(i).getContent())
                    .createdAt(postText.get(i).getPosts().getCreatedAt())
                    .createdBy(postText.get(i).getCreatedBy())
                    .updatedAt(postText.get(i).getPosts().getUpdatedAt())
                    .updatedBy(postText.get(i).getUpdatedBy())
                    .isLike(postText.get(i).getPosts().getIsLike())
                    .tags(tags.get(i))
                    .build();

            postTextReadResponsesList.add(postTextReadResponse);
        }

        ArrayList<PostTextReadResponse> responsesList = new ArrayList<PostTextReadResponse>();
        for(int i=0;i<postTextNum;i++) {
            responsesList.add(postTextReadResponsesList.get(postTextNum-i-1));
        }

        return responsesList;
    }

    private PostTextReadResponse readres(PostText postText, ArrayList<String> tags) {
        PostTextReadResponse postTextReadResponse = PostTextReadResponse.builder()
                .resultCode("Success")
                .type("text")
                .postId(postText.getPosts().getPostId())
                .textId(postText.getTextId())
                .title(postText.getTitle())
                .content(postText.getContent())
                .createdAt(postText.getPosts().getCreatedAt())
                .createdBy(postText.getCreatedBy())
                .updatedAt(postText.getPosts().getUpdatedAt())
                .updatedBy(postText.getUpdatedBy())
                .isLike(postText.getPosts().getIsLike())
                .tags(tags)
                .build();
        return postTextReadResponse;
    }
}
