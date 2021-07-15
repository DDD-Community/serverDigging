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
// 익스클루드 고치기
@ToString(exclude = {"userHasPostsList", "tagsList"})
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String username;
    private String email;
    private String password;
    private String provider;
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role;
    private String interest;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "user", cascade = CascadeType.ALL)
    private List<UserHasPosts> userHasPostsList;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "user", cascade = CascadeType.ALL)
    private List<Tags> tagsList;

//    public User update(String name, String picture) {
//        this.name = name;
//        this.picture = picture;
//        return this;
//    }

    public String getRoleKey() {
        return this.role.getKey();
    }

}
