package com.example.digging.domain.entity;

import lombok.*;
import lombok.experimental.Accessors;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Builder
@Accessors(chain = true)
@ToString(exclude = {"postLinkList", "postImgList", "postTextList", "userHasPostsList", "postTagList"})
public class Posts {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer postId;

    private Boolean isText;
    private Boolean isImg;
    private Boolean isLink;
    private Boolean isLike;
    private LocalDateTime createdAt;
    private String createdBy;
    private LocalDateTime updatedAt;
    private String updatedBy;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "posts", cascade = CascadeType.ALL)
    private List<PostImg> postImgList;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "posts", cascade = CascadeType.ALL)
    private List<PostLink> postLinkList;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "posts", cascade = CascadeType.ALL)
    private List<PostText> postTextList;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "posts", cascade = CascadeType.ALL)
    private List<UserHasPosts> userHasPostsList;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "posts", cascade = CascadeType.ALL)
    private List<PostTag> postTagList;
}
