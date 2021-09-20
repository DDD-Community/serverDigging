package com.example.digging.service;

import com.example.digging.domain.entity.*;
import com.example.digging.domain.network.request.PostImgApiRequest;
import com.example.digging.domain.network.response.*;
import com.example.digging.domain.repository.*;
import com.example.digging.ifs.CrudInterface;
import com.example.digging.util.SecurityUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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

    public PostImgReadResponse imgread(Integer postid) {
        User userInfo = SecurityUtil.getCurrentUsername().flatMap(userRepository::findOneWithAuthoritiesByUsername)
                .orElseThrow(() -> new RuntimeException("token 오류 입니다. 사용자를 찾을 수 없습니다."));

        Optional<UserHasPosts> optional = userHasPostsRepository.findByUser_UserIdAndPostsPostId(userInfo.getUserId(), postid);
        ArrayList<String> tagList = new ArrayList<String>();

        return optional
                .map(opt -> {
                    List<PostTag> tagopt = postTagRepository.findAllByPostsPostId(postid);
                    int tagNum = tagopt.size();
                    for(int i =0; i<tagNum; i++){
                        tagList.add(tagopt.get(i).getTags().getTags());
                    }
                    Optional<PostImg> postopt = Optional.ofNullable(postImgRepository.findByPostsPostId(postid));
                    return postopt
                            .map(posts -> {
                                PostImg readPost = posts;
                                ArrayList<ImgsApiResponse> imgsResponse = new ArrayList<>();
                                List<Imgs> images = imgsRepository.findAllByPostImg_PostsPostId(postid);
                                int imgsNum = images.size();
                                for(int i=0; i<imgsNum; i++) {
                                    ImgsApiResponse imgsApiResponse = ImgsApiResponse.builder()
                                            .id(images.get(i).getId())
                                            .imgUrl(images.get(i).getImgUrl())
                                            .build();
                                    imgsResponse.add(imgsApiResponse);
                                }
                                return readres(readPost, tagList, imgsResponse);
                            })
                            .orElseGet(() -> {
                                        PostImgReadResponse error = PostImgReadResponse.builder().resultCode("Error : post 정보 없음").build();
                                        return error;
                                    }
                            );
                })
                .orElseGet(
                        () -> {
                            PostImgReadResponse error = PostImgReadResponse.builder().resultCode("Error : user id, post id 오류").build();
                            return error;
                        }
                );

    }

    public ArrayList<PostImgReadResponse> allimgread() {
        User userInfo = SecurityUtil.getCurrentUsername().flatMap(userRepository::findOneWithAuthoritiesByUsername)
                .orElseThrow(() -> new RuntimeException("token 오류 입니다. 사용자를 찾을 수 없습니다."));

        Optional<User> optional = userRepository.findById(userInfo.getUserId());
        List<UserHasPosts> userHasPosts = userHasPostsRepository.findAllByUser_UserId(userInfo.getUserId());
        int userHasPostsNum = userHasPosts.size();

        ArrayList<PostImg> postImgs = new ArrayList<PostImg>();
        ArrayList<ArrayList> tags = new ArrayList();
        for(int i =0; i<userHasPostsNum; i++){
            if(userHasPosts.get(i).getPosts().getIsImg() == Boolean.TRUE) {
                postImgs.add(postImgRepository.findByPostsPostId(userHasPosts.get(i).getPosts().getPostId()));
                List<PostTag> nowTags = postTagRepository.findAllByPostsPostId(userHasPosts.get(i).getPosts().getPostId());
                int nowTagsSize = nowTags.size();
                ArrayList<String> tagStr = new ArrayList<String>();
                for(int j=0;j<nowTagsSize;j++){
                    tagStr.add(nowTags.get(j).getTags().getTags());
                }
                tags.add(tagStr);
            }
        }


        return optional.map(user -> allreadres(postImgs, tags)).orElseGet(()->{
            ArrayList<PostImgReadResponse> errorList = new ArrayList<PostImgReadResponse>();
            PostImgReadResponse error = PostImgReadResponse.builder().resultCode("Error").build();
            errorList.add(error);
            return errorList;
        });

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
                .type("img")
                .userName(postImg.getCreatedBy())
                .postId(postImg.getPosts().getPostId())
                .imgId(postImg.getImgId())
                .title(postImg.getTitle())
                .newTags(tags)
                .imgs(imgsResponse)
                .build();

        return postImgApiResponse;

    }

    private PostImgReadResponse readres(PostImg postImg, ArrayList<String> tags, ArrayList<ImgsApiResponse> imgsResponse) {
        PostImgReadResponse postImgReadResponse = PostImgReadResponse.builder()
                .resultCode("Success")
                .type("img")
                .postId(postImg.getPosts().getPostId())
                .imgId(postImg.getImgId())
                .title(postImg.getTitle())
                .createdAt(postImg.getPosts().getCreatedAt())
                .createdBy(postImg.getCreatedBy())
                .updatedAt(postImg.getPosts().getUpdatedAt())
                .updatedBy(postImg.getUpdatedBy())
                .isLike(postImg.getPosts().getIsLike())
                .tags(tags)
                .imgs(imgsResponse)
                .build();
        return postImgReadResponse;
    }

    private ArrayList<PostImgReadResponse> allreadres(ArrayList<PostImg> postImg, ArrayList<ArrayList> tags){

        int postImgNum = postImg.size();
        ArrayList<PostImgReadResponse> postImgReadResponsesList = new ArrayList<PostImgReadResponse>();

        for(int i=0; i<postImgNum;i++){
            ArrayList<ImgsApiResponse> imgsResponse = new ArrayList<>();
            System.out.println(postImgNum);
            List<Imgs> images = imgsRepository.findAllByPostImg_PostsPostId(postImg.get(i).getPosts().getPostId());
            int imgsNum = images.size();
            System.out.println(imgsNum);
            for(int j=0; j<imgsNum; j++) {
                ImgsApiResponse imgsApiResponse = ImgsApiResponse.builder()
                        .id(images.get(j).getId())
                        .imgUrl(images.get(j).getImgUrl())
                        .build();
                imgsResponse.add(imgsApiResponse);
            }

            PostImgReadResponse postImgReadResponse = PostImgReadResponse.builder()
                    .resultCode("Success")
                    .type("img")
                    .postId(postImg.get(i).getPosts().getPostId())
                    .imgId(postImg.get(i).getImgId())
                    .title(postImg.get(i).getTitle())
                    .createdAt(postImg.get(i).getPosts().getCreatedAt())
                    .createdBy(postImg.get(i).getCreatedBy())
                    .updatedAt(postImg.get(i).getPosts().getUpdatedAt())
                    .updatedBy(postImg.get(i).getUpdatedBy())
                    .isLike(postImg.get(i).getPosts().getIsLike())
                    .tags(tags.get(i))
                    .imgs(imgsResponse)
                    .build();

            postImgReadResponsesList.add(postImgReadResponse);
        }

        ArrayList<PostImgReadResponse> responsesList = new ArrayList<PostImgReadResponse>();
        for(int i=0;i<postImgNum;i++) {
            responsesList.add(postImgReadResponsesList.get(postImgNum-i-1));
        }

        return responsesList;
    }

}
