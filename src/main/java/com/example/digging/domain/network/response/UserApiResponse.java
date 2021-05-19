package com.example.digging.domain.network.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserApiResponse {
    private Integer id;
    private String username;
    private String email;
    private String password;
    private String provider;
    private String role;
    private String interest;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
