package com.example.digging.service;

import com.example.digging.domain.entity.*;
import com.example.digging.domain.network.Header;
import com.example.digging.domain.network.request.PostLinkApiRequest;
import com.example.digging.domain.network.request.PostTextApiRequest;
import com.example.digging.domain.network.response.PostLinkApiResponse;
import com.example.digging.domain.network.response.PostLinkReadResponse;
import com.example.digging.domain.network.response.PostTextApiResponse;
import com.example.digging.domain.network.response.PostTextReadResponse;
import com.example.digging.domain.repository.*;
import com.example.digging.ifs.CrudInterface;
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
    public Header<PostTextApiResponse> create(Header<PostTextApiRequest> request) {
        PostTextApiRequest body = request.getData();

        String[] tags = body.getTagsArr();
        ArrayList<String> newTagList = new ArrayList<String>();

        Posts posts = Posts.builder()
                .isText(Boolean.TRUE)
                .isImg(Boolean.FALSE)
                .isLink(Boolean.FALSE)
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

        PostText postText = PostText.builder()
                .posts(newPosts)
                .title(body.getTitle())
                .content(body.getContent())
                .createdAt(LocalDateTime.now())
                .createdBy(body.getUserName())
                .updatedAt(LocalDateTime.now())
                .updatedBy(body.getUserName())
                .build();

        PostText newPostText = postTextRepository.save(postText);

        return response(newPostText, newTagList);

    }

    @Override
    public Header<PostTextApiResponse> read(Integer id) {
        return null;
    }

    @Override
    public Header<PostTextApiResponse> update(Integer id, Header<PostTextApiRequest> request) {
        return null;
    }

    @Override
    public Header delete(Integer id) {
        return null;
    }

    public Header<PostTextReadResponse> textread(Integer userid, Integer postid) {
        Optional<UserHasPosts> optional = userHasPostsRepository.findByUserIdAndPostsPostId(userid, postid);
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
                            .orElseGet(() -> Header.ERROR("post 정보 없음"));
                })
                .orElseGet(()-> Header.ERROR("user id, post id 오류(데이터 없음)"));


    }

    public Header<ArrayList<PostTextReadResponse>> alltextread(Integer userid) {
        Optional<User> optional = userRepository.findById(userid);
        List<UserHasPosts> userHasPosts = userHasPostsRepository.findAllByUserId(userid);
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
        return optional.map(user -> allreadres(postTexts, tags)).orElseGet(()->Header.ERROR("없는 user"));

    }

    private Header<PostTextApiResponse> response(PostText postText, ArrayList<String> tags){

        PostTextApiResponse postTextApiResponse = PostTextApiResponse.builder()
                .userName(postText.getCreatedBy())
                .postId(postText.getPosts().getPostId())
                .textId(postText.getTextId())
                .title(postText.getTitle())
                .content(postText.getContent())
                .newTags(tags)
                .build();

        return Header.OK(postTextApiResponse);

    }

    private Header<ArrayList<PostTextReadResponse>> allreadres(ArrayList<PostText> postText, ArrayList<ArrayList> tags){

        int postTextNum = postText.size();
        ArrayList<PostTextReadResponse> postTextReadResponsesList = new ArrayList<PostTextReadResponse>();
        ArrayList<PostTextReadResponse> ResponsesList = new ArrayList<PostTextReadResponse>();
        for(int i=0; i<postTextNum;i++){
            PostTextReadResponse postTextReadResponse = PostTextReadResponse.builder()
                    .postId(postText.get(i).getPosts().getPostId())
                    .textId(postText.get(i).getTextId())
                    .title(postText.get(i).getTitle())
                    .content(postText.get(i).getContent())
                    .createdAt(postText.get(i).getCreatedAt())
                    .createdBy(postText.get(i).getCreatedBy())
                    .updatedAt(postText.get(i).getUpdatedAt())
                    .updatedBy(postText.get(i).getUpdatedBy())
                    .tags(tags.get(i))
                    .build();

            postTextReadResponsesList.add(postTextReadResponse);
        }

        ArrayList<PostTextReadResponse> responsesList = new ArrayList<PostTextReadResponse>();
        for(int i=0;i<postTextNum;i++) {
            responsesList.add(postTextReadResponsesList.get(postTextNum-i-1));
        }

        return Header.OK(responsesList);
    }

    private Header<PostTextReadResponse> readres(PostText postText, ArrayList<String> tags) {
        PostTextReadResponse postTextReadResponse = PostTextReadResponse.builder()
                .postId(postText.getPosts().getPostId())
                .textId(postText.getTextId())
                .title(postText.getTitle())
                .content(postText.getContent())
                .createdAt(postText.getCreatedAt())
                .createdBy(postText.getCreatedBy())
                .updatedAt(postText.getUpdatedAt())
                .updatedBy(postText.getUpdatedBy())
                .tags(tags)
                .build();
        return Header.OK(postTextReadResponse);
    }
}
