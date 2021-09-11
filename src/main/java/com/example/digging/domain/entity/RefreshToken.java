package com.example.digging.domain.entity;

import lombok.*;
import lombok.experimental.Accessors;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Builder
@Accessors(chain = true)
public class RefreshToken {

    @Id
    @Column(name = "username")
    private String username;

    private String token;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public RefreshToken updateValue(String token, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.token = token;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        return this;
    }

}