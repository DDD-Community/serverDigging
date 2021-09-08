package com.example.digging.domain.network;

import com.example.digging.domain.entity.Authority;
import com.example.digging.domain.entity.Tags;
import com.example.digging.domain.entity.UserHasPosts;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserDto {


    private Integer userId;

    //고유식별자
    private String oauthId;

    private Boolean activated;

    private String username;
    private String email;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String password;

    private String provider;

    private String interest;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;


}
