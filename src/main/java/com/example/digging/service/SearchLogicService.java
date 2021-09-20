package com.example.digging.service;


import com.example.digging.domain.entity.*;
import com.example.digging.domain.network.Search;
import com.example.digging.domain.network.SearchHeader;
import com.example.digging.domain.network.response.ImgsApiResponse;
import com.example.digging.domain.network.response.RecentDiggingResponse;
import com.example.digging.domain.repository.*;
import com.example.digging.util.SecurityUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.comparator.Comparators;

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

        HashMap<Integer, Integer> map = new HashMap<Integer, Integer>();

        for (int i=0; i<userHasPostsNum; i++) {

            Search search = Search.builder().build();

            if (userHasPostsList.get(i).getPosts().getIsLink() == Boolean.TRUE) {
                search.setAddPost(postLinkRepository.findByPostsPostId(userHasPostsList.get(i).getPosts().getPostId()).getPosts());
                search.setTitle(postLinkRepository.findByPostsPostId(userHasPostsList.get(i).getPosts().getPostId()).getTitle());
                search.setTagsList(postLinkRepository.findByPostsPostId(userHasPostsList.get(i).getPosts().getPostId()).getPosts().getPostTagList());
                search.setContent("");
            }
            if (userHasPostsList.get(i).getPosts().getIsText() == Boolean.TRUE) {
                search.setAddPost(postTextRepository.findByPostsPostId(userHasPostsList.get(i).getPosts().getPostId()).getPosts());
                search.setTitle(postTextRepository.findByPostsPostId(userHasPostsList.get(i).getPosts().getPostId()).getTitle());
                search.setTagsList(postTextRepository.findByPostsPostId(userHasPostsList.get(i).getPosts().getPostId()).getPosts().getPostTagList());
                search.setContent(postTextRepository.findByPostsPostId(userHasPostsList.get(i).getPosts().getPostId()).getContent());
            }
            if (userHasPostsList.get(i).getPosts().getIsImg() == Boolean.TRUE) {
                search.setAddPost(postImgRepository.findByPostsPostId(userHasPostsList.get(i).getPosts().getPostId()).getPosts());
                search.setTitle(postImgRepository.findByPostsPostId(userHasPostsList.get(i).getPosts().getPostId()).getTitle());
                search.setTagsList(postImgRepository.findByPostsPostId(userHasPostsList.get(i).getPosts().getPostId()).getPosts().getPostTagList());
                search.setContent("");
            }

            if (search.getTitle().contains(" ")) {
                search.setTitle(search.getTitle().replace(" ", ""));
            }
            if (search.getContent().contains(" ")) {
                search.setContent(search.getContent().replace(" ", ""));
            }

            ArrayList a_list = new ArrayList();
            if (search.getTitle().contains(keyword)) {
                //for문을 수행해 각 문자가 keyword 첫 글자값이 맞는지 확인한다
                for(int j=0; j<search.getTitle().length(); j++) {
                    if(search.getTitle().charAt(j) == keyword.charAt(0)) {
                        a_list.add(j);
                    }
                }

                //배열사이즈를 확인해 keyword 첫 글자 개수 만큼 다시 for 반복문을 수행하며 2자리씩 문자열을 잘라서 확인한다
                String pandan = "";
                for(int j=0; j<a_list.size(); j++) {
                    int start_idx = Integer.valueOf(a_list.get(j).toString());
                    pandan = search.getTitle().substring(start_idx, start_idx+keyword.length());
                    if(pandan.equals(keyword)) { //자른 문자열이 keyword와 같으면
                        search.setStr_count(search.getStr_count() + 1); //keyword 체크 카운트값을 증가시킨다
                    }
                }
                map.put(search.getAddPost().getPostId(), search.getStr_count());
            }

            ArrayList b_list = new ArrayList();
            if (search.getContent().contains(keyword)) {
                //for문을 수행해 각 문자가 keyword 첫 글자값이 맞는지 확인한다
                for(int j=0; j<search.getContent().length(); j++) {
                    if(search.getContent().charAt(j) == keyword.charAt(0)) {
                        b_list.add(j);
                    }
                }

                //배열사이즈를 확인해 keyword 첫 글자 개수 만큼 다시 for 반복문을 수행하며 2자리씩 문자열을 잘라서 확인한다
                String pandan = "";
                for(int j=0; j<b_list.size(); j++) {
                    int start_idx = Integer.valueOf(b_list.get(j).toString());
                    pandan = search.getContent().substring(start_idx, start_idx+keyword.length());
                    if(pandan.equals(keyword)) { //자른 문자열이 keyword와 같으면
                        search.setStr_count(search.getStr_count() + 1); //keyword 체크 카운트값을 증가시킨다
                    }
                }

                if ( map.containsKey(search.getAddPost().getPostId()) == true ) {
                    map.replace(search.getAddPost().getPostId(), search.getStr_count());
                } else {
                    map.put(search.getAddPost().getPostId(), search.getStr_count());
                }
            }

            ArrayList c_list = new ArrayList();
            for(int j=0;j<search.getTagsList().size();j++) {
                if (search.getTagsList().get(j).getTags().getTags().contains(keyword)) {
                    //for문을 수행해 각 문자가 keyword 첫 글자값이 맞는지 확인한다
                    for(int k=0; k<search.getTagsList().get(j).getTags().getTags().length(); k++) {
                        if(search.getTagsList().get(j).getTags().getTags().charAt(k) == keyword.charAt(0)) {
                            c_list.add(k);
                        }
                    }

                    //배열사이즈를 확인해 keyword 첫 글자 개수 만큼 다시 for 반복문을 수행하며 2자리씩 문자열을 잘라서 확인한다
                    String pandan = "";
                    for(int k=0; k<c_list.size(); k++) {
                        int start_idx = Integer.valueOf(c_list.get(k).toString());
                        pandan = search.getTagsList().get(j).getTags().getTags().substring(start_idx, start_idx+keyword.length());
                        if(pandan.equals(keyword)) { //자른 문자열이 keyword와 같으면
                            search.setStr_count(search.getStr_count() + 1); //keyword 체크 카운트값을 증가시킨다
                        }
                    }

                    if ( map.containsKey(search.getAddPost().getPostId()) == true ) {
                        map.replace(search.getAddPost().getPostId(), search.getStr_count());
                    } else {
                        map.put(search.getAddPost().getPostId(), search.getStr_count());
                    }

                }
            }
        }


        List<Integer> keySetList = new ArrayList<>(map.keySet());
        if(keySetList.size() == 0) {
            ArrayList<RecentDiggingResponse> errorList = new ArrayList<RecentDiggingResponse>();
            RecentDiggingResponse error = RecentDiggingResponse.builder().resultCode("해당 Keyword를 가진 Posts 없음").build();
            errorList.add(error);
            return SearchHeader.OK(0, errorList);
        }
        List<Integer> valueSetList = new ArrayList<>();

        // KeyWord 언급 내림차순
        Collections.sort(keySetList, (o1, o2) -> (map.get(o2).compareTo(map.get(o1))));


        for(Integer key : keySetList) {
            valueSetList.add(map.get(key));
        }

        Integer[] arr = new Integer[valueSetList.size()];
        for(int c=0;c<valueSetList.size();c++) {
            arr[c] = valueSetList.get(c);
        }

        Map<Integer, Integer> arrmap = new HashMap<Integer, Integer>();
        ArrayList<Integer> overOne = new ArrayList<>();
        for(Integer key : arr) {
            arrmap.put(key, arrmap.getOrDefault(key, 0)+1);
            if (arrmap.get(key) > 1){
                overOne.add(key);
            }
        }

        HashSet<Integer> hashSet = new HashSet<>();
        for(Integer item : overOne){
            hashSet.add(item);
        }


        ArrayList<Optional<Posts>> orderPostsList = new ArrayList<>();

        for(int r=0;r<keySetList.size();r++) {

            if(!hashSet.contains(valueSetList.get(r))) {
                orderPostsList.add(postsRepository.findById(keySetList.get(r)));
            } else {
                Map<Integer, LocalDateTime> mapping = new LinkedHashMap<>();
                for(int i=r; i<r+arrmap.get(valueSetList.get(r)); i++){
                    mapping.put(postsRepository.findById(keySetList.get(i)).get().getPostId(), postsRepository.findById(keySetList.get(i)).get().getUpdatedAt());
                }
                r += arrmap.get(valueSetList.get(r));
                r -= 1;
                Map<Integer, LocalDateTime> result = sortMapByValue(mapping);
                ArrayList<Optional<Posts>> unPostsList = new ArrayList<>();
                for (Integer key : result.keySet()) {
                    unPostsList.add(postsRepository.findById(key));
                }
                for (int un =1; un <= unPostsList.size();un++){
                    orderPostsList.add(postsRepository.findById(unPostsList.get(unPostsList.size() - un).get().getPostId()));
                }
            }
        }

        ArrayList<RecentDiggingResponse> keywordDiggingList = new ArrayList<RecentDiggingResponse>();
        for(int i =0; i<orderPostsList.size(); i++){
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
                keywordDiggingList.add(makingResponse);
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
                keywordDiggingList.add(makingResponse);
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
                keywordDiggingList.add(makingResponse);
            }
        }


        return SearchHeader.OK(orderPostsList.size(), keywordDiggingList);
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
