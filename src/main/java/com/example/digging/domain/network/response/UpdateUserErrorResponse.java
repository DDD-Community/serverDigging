package com.example.digging.domain.network.response;

import com.example.digging.domain.network.UserDto;
import com.example.digging.domain.network.request.UpdateUserRequest;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdateUserErrorResponse  extends UserDto {
    private String usernameError;
    private String emailError;

    public UpdateUserErrorResponse(Boolean nick, Boolean email, String errorMessage) {
        if (nick == Boolean.TRUE) {
            this.usernameError = errorMessage;
        } else {
            this.emailError = errorMessage;
        }
    }

    public UpdateUserErrorResponse(String errorMessage, String errorMessage2) {
        this.usernameError = errorMessage;
        this.emailError = errorMessage2;
    }
}
