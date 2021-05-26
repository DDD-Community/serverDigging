package com.example.digging.service;

import com.example.digging.domain.entity.*;
import com.example.digging.domain.network.Header;
import com.example.digging.domain.network.response.PostLinkReadResponse;
import com.example.digging.domain.network.response.PostTextReadResponse;
import com.example.digging.domain.network.response.RecentDiggingResponse;
import com.example.digging.domain.repository.*;
import com.example.digging.util.UrlTypeValidation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;

@Service
public class MainPageLogicService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PostsRepository postsRepository;

    @Autowired
    private UserHasPostsRepository userHasPostsRepository;

    @Autowired
    private PostLinkRepository postLinkRepository;

    @Autowired
    private PostTextRepository postTextRepository;

    @Autowired
    private PostTagRepository postTagRepository;

    //<ArrayList<>>
    public Header<ArrayList<RecentDiggingResponse>> recentPostsRead(Integer userid) {
        Optional<User> optional = userRepository.findById(userid);
        List<UserHasPosts> userHasPostsList = userHasPostsRepository.findAllByUserId(userid);
        int userHasPostsNum = userHasPostsList.size();
        ArrayList<Integer> postIdList = new ArrayList<Integer>();
        for(int i =0; i<userHasPostsNum; i++){
            postIdList.add(userHasPostsList.get(i).getPosts().getPostId());
        }

        ArrayList<Optional<Posts>> postsList = new ArrayList<>();
        Map<Integer, LocalDateTime> map = new LinkedHashMap<>();
        ArrayList<Optional<Posts>> orderPostsList = new ArrayList<>();
        for(int i =0; i<userHasPostsNum; i++){
            postsList.add(postsRepository.findById(postIdList.get(i)));
            map.put(postsRepository.findById(postIdList.get(i)).get().getPostId(),postsRepository.findById(postIdList.get(i)).get().getUpdatedAt());
        }
        Map<Integer, LocalDateTime> result = sortMapByValue(map);

        for (Integer key : result.keySet()) {
            orderPostsList.add(postsRepository.findById(key));
        }


        ArrayList responseList = new ArrayList();
        ArrayList<ArrayList> tags = new ArrayList();
        ArrayList<RecentDiggingResponse> recentDiggingList = new ArrayList<RecentDiggingResponse>();
        for(int i =0; i<userHasPostsNum; i++){
            if(orderPostsList.get(i).get().getIsLink() == Boolean.TRUE) {
                responseList.add(postLinkRepository.findByPostsPostId(orderPostsList.get(i).get().getPostId()));
                List<PostTag> nowTags = postTagRepository.findAllByPostsPostId(orderPostsList.get(i).get().getPostId());
                int nowTagsSize = nowTags.size();
                ArrayList<String> tagStr = new ArrayList<String>();
                for(int j=0;j<nowTagsSize;j++){
                    tagStr.add(nowTags.get(j).getTags().getTags());
                }
                tags.add(tagStr);
            }

            if(orderPostsList.get(i).get().getIsText() == Boolean.TRUE) {
                responseList.add(postTextRepository.findByPostsPostId(orderPostsList.get(i).get().getPostId()));
                List<PostTag> nowTags = postTagRepository.findAllByPostsPostId(orderPostsList.get(i).get().getPostId());
                int nowTagsSize = nowTags.size();
                ArrayList<String> tagStr = new ArrayList<String>();
                for(int j=0;j<nowTagsSize;j++){
                    tagStr.add(nowTags.get(j).getTags().getTags());
                }
                tags.add(tagStr);
            }
        }
        ArrayList<RecentDiggingResponse> orderrecentDiggingList = new ArrayList<RecentDiggingResponse>();
        int number = recentDiggingList.size();
        if(number<=10){
            for(int i=0;i<number;i++) {
                orderrecentDiggingList.add(recentDiggingList.get(number-i-1));
            }
        } else {
            for(int i=0;i<10;i++){
                orderrecentDiggingList.add(recentDiggingList.get(number-i-1));
            }
        }

        return optional.map(user -> Header.OK(orderrecentDiggingList)).orElseGet(()->Header.ERROR("user 없음"));
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
