package com.example.service.service;

import com.example.service.dto.request.ForgotPasswordRequestDto;
import com.example.service.dto.request.ResetPasswordRequestDto;
import com.example.service.dto.request.UserLoginRequestDto;
import com.example.service.dto.response.ForgotPasswordResponseDto;
import com.example.service.dto.response.ResetPasswordResponseDto;
import jakarta.servlet.http.HttpServletRequest;

public interface AuthService {

    String login(UserLoginRequestDto req, String ip, HttpServletRequest request);

    ResetPasswordResponseDto resetPassword(ResetPasswordRequestDto request);

    ForgotPasswordResponseDto forgotPassword(ForgotPasswordRequestDto request);
}
