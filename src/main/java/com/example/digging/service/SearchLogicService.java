package com.example.digging.service;


import com.example.digging.domain.entity.*;
import com.example.digging.domain.network.SearchHeader;
import com.example.digging.domain.network.response.ImgsApiResponse;
import com.example.digging.domain.network.response.RecentDiggingResponse;
import com.example.digging.domain.repository.*;
import com.example.digging.util.SecurityUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;

@Service
public class SearchLogicService {

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
    private PostImgRepository postImgRepository;

    @Autowired
    private ImgsRepository imgsRepository;

    @Autowired
    private PostLinkRepository postLinkRepository;

    @Autowired
    private UserHasPostsRepository userHasPostsRepository;


    public SearchHeader<ArrayList<RecentDiggingResponse>> searchByKeyword(String keyword) {

        User userInfo = SecurityUtil.getCurrentUsername().flatMap(userRepository::findOneWithAuthoritiesByUsername)
                .orElseThrow(() -> new RuntimeException("token 오류 입니다. 사용자를 찾을 수 없습니다."));

        List<UserHasPosts> userHasPostsList = userHasPostsRepository.findAllByUser_UserId(userInfo.getUserId());
        int userHasPostsNum = userHasPostsList.size();

        return null;
    }

    public SearchHeader<ArrayList<RecentDiggingResponse>> searchByTag(String tag) {

        User userInfo = SecurityUtil.getCurrentUsername().flatMap(userRepository::findOneWithAuthoritiesByUsername)
                .orElseThrow(() -> new RuntimeException("token 오류 입니다. 사용자를 찾을 수 없습니다."));

        Optional<Tags> findTag = tagsRepository.findByTagsAndUser_Username(tag, userInfo.getUsername());

        return findTag
                .map(findone -> {
                    List<PostTag> postTagList = postTagRepository.findAllByTagsTagId(findone.getTagId());

                    Integer postsTotalNum = postTagList.size();

                    ArrayList<Posts> postsList = new ArrayList<>();
                    Map<Integer, LocalDateTime> map = new LinkedHashMap<>();
                    ArrayList<Optional<Posts>> orderPostsList = new ArrayList<>();
                    for(int i =0; i<postsTotalNum; i++){
                        postsList.add(postTagList.get(i).getPosts());
                        map.put(postTagList.get(i).getPosts().getPostId(), postTagList.get(i).getPosts().getUpdatedAt());
                    }

                    Map<Integer, LocalDateTime> result = sortMapByValue(map);

                    for (Integer key : result.keySet()) {
                        orderPostsList.add(postsRepository.findById(key));
                    }

                    ArrayList<RecentDiggingResponse> recentDiggingList = new ArrayList<RecentDiggingResponse>();
                    for(int i =0; i<postsTotalNum; i++){
                        if(orderPostsList.get(i).get().getIsLink() == Boolean.TRUE) {
                            PostLink newlink = postLinkRepository.findByPostsPostId(orderPostsList.get(i).get().getPostId());
                            List<PostTag> nowTags = postTagRepository.findAllByPostsPostId(orderPostsList.get(i).get().getPostId());
                            int nowTagsSize = nowTags.size();
                            ArrayList<String> tagStr = new ArrayList<String>();
                            for(int j=0;j<nowTagsSize;j++){
                                tagStr.add(nowTags.get(j).getTags().getTags());
                            }
                            RecentDiggingResponse makingResponse = RecentDiggingResponse.builder()
                                    .resultCode("Success")
                                    .type("link")
                                    .postId(newlink.getPosts().getPostId())
                                    .linkId(newlink.getLinkId())
                                    .title(newlink.getTitle())
                                    .url(newlink.getUrl())
                                    .createdAt(newlink.getCreatedAt())
                                    .createdBy(newlink.getCreatedBy())
                                    .updatedAt(newlink.getPosts().getUpdatedAt())
                                    .updatedBy(newlink.getUpdatedBy())
                                    .isLike(newlink.getPosts().getIsLike())
                                    .tags(tagStr)
                                    .build();
                            recentDiggingList.add(makingResponse);
                        }

                        if(orderPostsList.get(i).get().getIsText() == Boolean.TRUE) {
                            PostText newtext = postTextRepository.findByPostsPostId(orderPostsList.get(i).get().getPostId());
                            List<PostTag> nowTags = postTagRepository.findAllByPostsPostId(orderPostsList.get(i).get().getPostId());
                            int nowTagsSize = nowTags.size();
                            ArrayList<String> tagStr = new ArrayList<String>();
                            for(int j=0;j<nowTagsSize;j++){
                                tagStr.add(nowTags.get(j).getTags().getTags());
                            }
                            RecentDiggingResponse makingResponse = RecentDiggingResponse.builder()
                                    .resultCode("Success")
                                    .type("text")
                                    .postId(newtext.getPosts().getPostId())
                                    .textId(newtext.getTextId())
                                    .title(newtext.getTitle())
                                    .content(newtext.getContent())
                                    .createdAt(newtext.getCreatedAt())
                                    .createdBy(newtext.getCreatedBy())
                                    .updatedAt(newtext.getPosts().getUpdatedAt())
                                    .updatedBy(newtext.getUpdatedBy())
                                    .isLike(newtext.getPosts().getIsLike())
                                    .tags(tagStr)
                                    .build();
                            recentDiggingList.add(makingResponse);
                        }

                        if(orderPostsList.get(i).get().getIsImg() == Boolean.TRUE) {
                            PostImg newimg = postImgRepository.findByPostsPostId(orderPostsList.get(i).get().getPostId());
                            List<PostTag> nowTags = postTagRepository.findAllByPostsPostId(orderPostsList.get(i).get().getPostId());
                            int nowTagsSize = nowTags.size();
                            ArrayList<String> tagStr = new ArrayList<String>();
                            for(int j=0;j<nowTagsSize;j++){
                                tagStr.add(nowTags.get(j).getTags().getTags());
                            }

                            ArrayList<ImgsApiResponse> imgsResponse = new ArrayList<>();
                            List<Imgs> images = imgsRepository.findAllByPostImg_PostsPostId(newimg.getPosts().getPostId());
                            int imgsNum = images.size();
                            for(int j=0; j<imgsNum; j++) {
                                ImgsApiResponse imgsApiResponse = ImgsApiResponse.builder()
                                        .id(images.get(j).getId())
                                        .imgUrl(images.get(j).getImgUrl())
                                        .build();
                                imgsResponse.add(imgsApiResponse);
                            }

                            RecentDiggingResponse makingResponse = RecentDiggingResponse.builder()
                                    .resultCode("Success")
                                    .type("img")
                                    .postId(newimg.getPosts().getPostId())
                                    .imgId(newimg.getImgId())
                                    .title(newimg.getTitle())
                                    .createdAt(newimg.getCreatedAt())
                                    .createdBy(newimg.getCreatedBy())
                                    .updatedAt(newimg.getPosts().getUpdatedAt())
                                    .updatedBy(newimg.getUpdatedBy())
                                    .isLike(newimg.getPosts().getIsLike())
                                    .tags(tagStr)
                                    .totalImgNum(imgsResponse.size())
                                    .imgs(imgsResponse)
                                    .build();
                            recentDiggingList.add(makingResponse);
                        }
                    }

                    ArrayList<RecentDiggingResponse> orderrecentDiggingList = new ArrayList<RecentDiggingResponse>();
                    int number = recentDiggingList.size();
                    for(int i=0;i<number;i++) {
                        orderrecentDiggingList.add(recentDiggingList.get(number-i-1));
                    }
                    return orderrecentDiggingList;
                }

                )
                .map( orderlist -> SearchHeader.OK(orderlist.size(), orderlist))
                .orElseGet(()->{
                    ArrayList<RecentDiggingResponse> errorList = new ArrayList<RecentDiggingResponse>();
                    RecentDiggingResponse error = RecentDiggingResponse.builder().resultCode("해당 Tag를 가진 Posts 없음").build();
                    errorList.add(error);
                    return SearchHeader.OK(0, errorList);
                });

    }

    public static LinkedHashMap<Integer, LocalDateTime> sortMapByValue(Map<Integer, LocalDateTime> map) {
        List<Map.Entry<Integer, LocalDateTime>> entries = new LinkedList<>(map.entrySet());
        Collections.sort(entries, (o1, o2) -> o1.getValue().compareTo(o2.getValue()));

        LinkedHashMap<Integer, LocalDateTime> result = new LinkedHashMap<>();
        for (Map.Entry<Integer, LocalDateTime> entry : entries) {
            result.put(entry.getKey(), entry.getValue());
        }
        return result;
    }
}
