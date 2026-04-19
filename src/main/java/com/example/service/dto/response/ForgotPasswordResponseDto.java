package com.example.service.dto.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ForgotPasswordResponseDto {

    private String message;
    private String email;

    public ForgotPasswordResponseDto(String message, String email) {
        this.message = message;
        this.email = email;
    }
}
