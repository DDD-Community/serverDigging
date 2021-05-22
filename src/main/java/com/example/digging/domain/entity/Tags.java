package com.example.digging.domain.entity;

import lombok.*;
import lombok.experimental.Accessors;

import javax.persistence.*;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Builder
@Accessors(chain = true)
@ToString(exclude = {"postTagList"})
public class Tags {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer tagId;

    private String tags;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "tags", cascade = CascadeType.ALL)
    private List<PostTag> postTagList;

    @ManyToOne
    private User user;
}
