package com.example.digging.domain.network;

import lombok.*;

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

    private Date accessTokenExpiresIn;
    private String refreshToken;
}