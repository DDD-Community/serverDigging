package com.example.digging.domain.network.response;

import com.example.digging.domain.entity.User;
import com.example.digging.domain.network.TokenDto;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class ErrorResponse extends TokenDto {
    private String errorMessage;
    private String errorCode;

    public ErrorResponse(String errorMessage) {
        this.errorMessage = errorMessage;
        this.errorCode = "404";
    }
    public ErrorResponse(String errorMessage, String errorCode) {
        this.errorMessage = errorMessage;
        this.errorCode = errorCode;
    }
}