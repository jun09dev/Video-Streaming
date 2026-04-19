package com.example.service.dto.response;

public class ResetPasswordResponseDto {

    private String message;

    public ResetPasswordResponseDto() {
    }

    public ResetPasswordResponseDto(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
