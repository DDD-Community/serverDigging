package com.example.digging.service;

import com.example.digging.domain.entity.*;
import com.example.digging.domain.network.Header;
import com.example.digging.domain.network.request.PostLinkApiRequest;
import com.example.digging.domain.network.response.PostLinkApiResponse;
import com.example.digging.domain.network.response.PostLinkReadResponse;
import com.example.digging.domain.network.response.PostTextReadResponse;
import com.example.digging.domain.repository.*;
import com.example.digging.ifs.CrudInterface;
import com.example.digging.util.UrlTypeValidation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.support.NullValue;
import org.springframework.stereotype.Service;


import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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


    @Override
    public PostLinkApiResponse create(PostLinkApiRequest request) {
        PostLinkApiRequest body = request;
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
            PostLinkApiResponse error = PostLinkApiResponse.builder().resultCode("Error : 유효하지 않은 URL").build();
            return error;
        }
    }

    @Override
    public PostLinkApiResponse read(Integer id) {
        return null;
    }

    public PostLinkReadResponse linkread(Integer userid, Integer postid) {
        Optional<UserHasPosts> optional = userHasPostsRepository.findByUserIdAndPostsPostId(userid, postid);
        ArrayList<String> tagList = new ArrayList<String>();

        return optional
                .map(opt -> {
                    List<PostTag> tagopt = postTagRepository.findAllByPostsPostId(postid);
                    int tagNum = tagopt.size();
                    for(int i =0; i<tagNum; i++){
                        tagList.add(tagopt.get(i).getTags().getTags());
                    }
                    Optional<PostLink> linkopt = Optional.ofNullable(postLinkRepository.findByPostsPostId(postid));
                    return linkopt
                            .map(posts -> {
                                PostLink readPost = posts;
                                return readres(readPost, tagList);
                            })
                            .orElseGet(() -> {
                                PostLinkReadResponse error = PostLinkReadResponse.builder().resultCode("Error : post 정보 없음").build();
                                return error;
                                    }
                            );
                })
                .orElseGet(
                        () -> {
                            PostLinkReadResponse error = PostLinkReadResponse.builder().resultCode("Error : user id, post id 오류").build();
                            return error;
                        }
                );

    }

    @Override
    public PostLinkApiResponse update(Integer id, PostLinkApiRequest request) {
        return null;
    }

    @Override
    public PostLinkApiResponse delete(Integer id) {
        return null;
    }


    private PostLinkReadResponse readres(PostLink postLink, ArrayList<String> tags) {
        PostLinkReadResponse postLinkReadResponse = PostLinkReadResponse.builder()
                .resultCode("Success")
                .postId(postLink.getPosts().getPostId())
                .linkId(postLink.getLinkId())
                .title(postLink.getTitle())
                .url(postLink.getUrl())
                .createdAt(postLink.getPosts().getCreatedAt())
                .createdBy(postLink.getCreatedBy())
                .updatedAt(postLink.getPosts().getUpdatedAt())
                .updatedBy(postLink.getUpdatedBy())
                .isLike(postLink.getPosts().getIsLike())
                .tags(tags)
                .build();
        return postLinkReadResponse;
    }

    public ArrayList<PostLinkReadResponse> alllinkread(Integer userid) {
        Optional<User> optional = userRepository.findById(userid);
        List<UserHasPosts> userHasPosts = userHasPostsRepository.findAllByUserId(userid);
        int userHasPostsNum = userHasPosts.size();

        ArrayList<PostLink> postLinks = new ArrayList<PostLink>();
        ArrayList<ArrayList> tags = new ArrayList();
        for(int i =0; i<userHasPostsNum; i++){
            if(userHasPosts.get(i).getPosts().getIsLink() == Boolean.TRUE) {
                postLinks.add(postLinkRepository.findByPostsPostId(userHasPosts.get(i).getPosts().getPostId()));
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
        return optional.map(user -> allreadres(postLinks, tags))
                .orElseGet(()->{
                    ArrayList<PostLinkReadResponse> errorList = new ArrayList<PostLinkReadResponse>();
                    PostLinkReadResponse error = PostLinkReadResponse.builder().resultCode("Error").build();
                    errorList.add(error);
                    return errorList;
                });

    }

    private ArrayList<PostLinkReadResponse> allreadres(ArrayList<PostLink> postLink, ArrayList<ArrayList> tags){

        int postLinkNum = postLink.size();
        ArrayList<PostLinkReadResponse> postLinkReadResponsesList = new ArrayList<PostLinkReadResponse>();

        for(int i=0; i<postLinkNum;i++){
            PostLinkReadResponse postLinkReadResponse = PostLinkReadResponse.builder()
                    .resultCode("Success")
                    .postId(postLink.get(i).getPosts().getPostId())
                    .linkId(postLink.get(i).getLinkId())
                    .title(postLink.get(i).getTitle())
                    .url(postLink.get(i).getUrl())
                    .createdAt(postLink.get(i).getPosts().getCreatedAt())
                    .createdBy(postLink.get(i).getCreatedBy())
                    .updatedAt(postLink.get(i).getPosts().getUpdatedAt())
                    .updatedBy(postLink.get(i).getUpdatedBy())
                    .isLike(postLink.get(i).getPosts().getIsLike())
                    .tags(tags.get(i))
                    .build();

            postLinkReadResponsesList.add(postLinkReadResponse);
        }

        ArrayList<PostLinkReadResponse> responsesList = new ArrayList<PostLinkReadResponse>();
        for(int i=0;i<postLinkNum;i++) {
            responsesList.add(postLinkReadResponsesList.get(postLinkNum-i-1));
        }

        return responsesList;
    }

    private PostLinkApiResponse response(PostLink postLink, String valid, ArrayList<String> tags){

        PostLinkApiResponse postLinkApiResponse = PostLinkApiResponse.builder()
                .resultCode("Success")
                .userName(postLink.getCreatedBy())
                .postId(postLink.getPosts().getPostId())
                .linkId(postLink.getLinkId())
                .title(postLink.getTitle())
                .urlCheck(valid)
                .newTags(tags)
                .build();

        return postLinkApiResponse;

    }
}
