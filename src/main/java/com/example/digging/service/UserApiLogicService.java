package com.example.digging.service;

import com.example.digging.domain.entity.*;
import com.example.digging.domain.network.request.UserApiRequest;
import com.example.digging.domain.network.response.*;
import com.example.digging.domain.repository.*;
import com.example.digging.ifs.CrudInterface;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class UserApiLogicService implements CrudInterface<UserApiRequest, UserApiResponse> {

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
    private PostLinkRepository postLinkRepository;

    @Autowired
    private UserHasPostsRepository userHasPostsRepository;

    @Override
    public UserApiResponse create(UserApiRequest request) {
        UserApiRequest userApiRequest = request;

        User user = User.builder()
                .username(userApiRequest.getUsername())
                .email(userApiRequest.getEmail())
                .password(userApiRequest.getPassword())
                .provider(userApiRequest.getProvider())
                .interest(userApiRequest.getInterest())
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        User newUser = userRepository.save(user);

        return response(newUser);
    }

    @Override
    public UserApiResponse read(Integer id) {

        Optional<User> optional = userRepository.findById(id);
        System.out.println(optional);
        return optional
                .map(user -> response(user))
                .orElseGet(
                        ()->{UserApiResponse userApiResponse = UserApiResponse.builder()
                                .resultCode("Error : 데이터 없음")
                                .build();
                            return userApiResponse;}
                );
    }

    @Override
    public UserApiResponse update(Integer id, UserApiRequest request) {
        UserApiRequest userApiRequest = request;
        Optional<User> optional = userRepository.findById(id);
        System.out.println(userApiRequest);
        System.out.println(optional);

        return optional
                .map(user -> {
                      user
//                          .setAuthority("ROLE_GUEST")
                            .setUpdatedAt(LocalDateTime.now())
                            ;
                    return user;
                })
                .map(user -> userRepository.save(user))
                .map(updateUser -> response(updateUser))
                .orElseGet(
                        ()->{UserApiResponse userApiResponse = UserApiResponse.builder()
                                .resultCode("Error : 데이터 없음")
                                .build();
                            return userApiResponse;}
                );

    }

    @Override
    public UserApiResponse delete(Integer id) {

        System.out.println(id);
        Optional<User> optional = userRepository.findById(id);
        return optional
                .map(user -> {
                    userRepository.delete(user);
                    UserApiResponse userApiResponse = UserApiResponse.builder()
                            .resultCode("Delete Success")
                            .build();
                    return userApiResponse;
                })
                .orElseGet(()->{UserApiResponse userApiResponse = UserApiResponse.builder()
                        .resultCode("Error : 데이터 없음")
                        .build();
                    return userApiResponse;}
                );

    }

    public PostsResponse deletePost(Integer userid, Integer postid) {

        Optional<UserHasPosts> optional = userHasPostsRepository.findByUserIdAndPostsPostId(userid, postid);

        return optional
                .map(opt -> {
                    Posts posts = opt.getPosts();

                    List<PostTag> postTagList = posts.getPostTagList();
                    postsRepository.delete(posts);
                    userHasPostsRepository.delete(opt);

                    for (int i =0; i<postTagList.size(); i++) {
                        postTagRepository.delete(postTagList.get(i));
                        if(postTagList.get(i).getTags().getPostTagList().isEmpty()){
                            tagsRepository.delete(postTagList.get(i).getTags());
                        }

                    }


                    PostsResponse postsResponse = PostsResponse.builder()
                            .resultCode("Delete Success")
                            .build();
                    return postsResponse;


                })
                .orElseGet(
                        ()->{
                            PostsResponse erros = PostsResponse.builder()
                                    .resultCode("Error : 데이터 없음")
                                    .build();
                            return erros;
                        }
                );


    }

    public TotalTagResponse getUserTotalTags(Integer id) {

        List<Tags> userTagList = tagsRepository.findAllByUserId(id);
        int userTagNum = userTagList.size();
        ArrayList<String> tagStr = new ArrayList<String>();

        for (int i =0;i<userTagNum;i++) {
            tagStr.add(userTagList.get(i).getTags());
        }

        if (userTagNum>0){
            TotalTagResponse totalTagResponse = TotalTagResponse.builder()
                    .resultCode("Success")
                    .totalNum(userTagNum)
                    .totalTags(tagStr)
                    .build();
            return totalTagResponse;
        }else{
            TotalTagResponse totalTagResponse = TotalTagResponse.builder()
                    .resultCode("데이터 없음")
                    .build();
            return totalTagResponse;
        }


    }

    public PostsResponse setLike(Integer userid, Integer postid) {

        Optional<UserHasPosts> optional = userHasPostsRepository.findByUserIdAndPostsPostId(userid, postid);

        return optional
                .map(opt -> {
                    Posts posts = opt.getPosts();
                    posts.setIsLike(!posts.getIsLike())
                            .setUpdatedAt(LocalDateTime.now())
                    ;
                    if(posts.getIsText()==Boolean.TRUE){
                        PostText postText = postTextRepository.findByPostsPostId(opt.getPosts().getPostId());
                        postTextRepository.save(postText.setUpdatedAt(LocalDateTime.now()));
                    }

                    if(posts.getIsLink()==Boolean.TRUE){
                        PostLink postLink = postLinkRepository.findByPostsPostId(opt.getPosts().getPostId());
                        postLinkRepository.save(postLink.setUpdatedAt(LocalDateTime.now()));
                    }
                    return posts;
                })
                .map(posts -> postsRepository.save(posts))
                .map(updatePost -> postres(updatePost))
                .orElseGet(
                        ()->{
                            PostsResponse errres = PostsResponse.builder()
                                    .resultCode("데이터 없음")
                                    .build();
                            return errres;
                        }
                );
    }

    public GetPostNumByTypeResponse getPostNumByType(Integer id) {

        List<UserHasPosts> userHasPostsList = userHasPostsRepository.findAllByUserId(id);
        int postsNum = userHasPostsList.size();
        Integer textNum = 0; Integer imgNum = 0; Integer linkNum = 0;
        for(int i=0;i<postsNum;i++){
            if (userHasPostsList.get(i).getPosts().getIsText() == Boolean.TRUE) {textNum += 1;}
            if (userHasPostsList.get(i).getPosts().getIsImg() == Boolean.TRUE) {imgNum += 1;}
            if (userHasPostsList.get(i).getPosts().getIsLink() == Boolean.TRUE) {linkNum += 1;}
        }

        return numres(textNum, imgNum, linkNum, id);
    }

    private GetPostNumByTypeResponse numres(Integer text, Integer img, Integer link, Integer userid) {
        Optional<User> user = userRepository.findById(userid);
        String username = user.map(finduser -> finduser.getUsername()).orElseGet(()->"User Error");

        GetPostNumByTypeResponse getPostNumByTypeResponse = GetPostNumByTypeResponse.builder()
                .resultCode("Success")
                .userName(username)
                .totalText(text)
                .totalImg(img)
                .totalLink(link)
                .build();

        return getPostNumByTypeResponse;
    }


    private UserApiResponse response(User user){
        UserApiResponse userApiResponse = UserApiResponse.builder()
                .resultCode("Success")
                .id(user.getId())
                .username(user.getUsername())
                .email(user.getEmail())
                .password(user.getPassword())
                .provider(user.getProvider())
//                .role(String.valueOf(user.getAuthority()))
                .interest(user.getInterest())
                .createdAt(user.getCreatedAt())
                .updatedAt(user.getUpdatedAt())
                .build();

        return userApiResponse;

    }

    private PostsResponse postres(Posts posts) {
        PostsResponse postsResponse = PostsResponse.builder()
                .resultCode("Success")
                .postId(posts.getPostId())
                .isText(posts.getIsText())
                .isImg(posts.getIsImg())
                .isLink(posts.getIsLink())
                .isLike(posts.getIsLike())
                .createdAt(posts.getCreatedAt())
                .createdBy(posts.getCreatedBy())
                .updatedAt(posts.getUpdatedAt())
                .updatedBy(posts.getUpdatedBy())
                .build();
        String typeStr = null;
        if (postsResponse.getIsText() == Boolean.TRUE) {
            typeStr = "text";
        }
        if (postsResponse.getIsImg() == Boolean.TRUE) {
            typeStr = "img";
        }
        if (postsResponse.getIsLink() == Boolean.TRUE) {
            typeStr = "link";
        }
        postsResponse.setType(typeStr);
        return postsResponse;
    }

}
