package com.example.digging.domain.network;

import lombok.*;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;
import java.util.Date;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TokenDto {

    private String grantType;

    private String accessToken;

    private LocalDateTime accessTokenExpiresIn;
    private String refreshToken;
}