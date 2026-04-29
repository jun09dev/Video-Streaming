package com.example.service.service;

import com.example.service.dto.request.UserCreatRequestDto;
import com.example.service.dto.response.UserCreatResponseDto;
import com.example.service.dto.response.UserProfileResponseDto;
import com.example.service.dto.response.UserResponseDto;

public interface UserService {

    UserCreatResponseDto createUser(UserCreatRequestDto req);
    UserResponseDto updateUsername(String email, String newUsername);


}
