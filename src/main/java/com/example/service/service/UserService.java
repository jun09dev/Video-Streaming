package com.example.service.service;

import com.example.service.dto.request.UserCreatRequestDto;
import com.example.service.dto.response.UserCreatResponseDto;

public interface UserService {

    UserCreatResponseDto createUser(UserCreatRequestDto req);


}
