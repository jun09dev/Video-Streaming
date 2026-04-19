package com.example.service.service;

import com.example.service.dto.request.UserLoginRequestDto;
import io.jsonwebtoken.Claims;

import java.util.Map;

public interface JwtService {

    String generateToken(Map<String, Object> extraData);

    Claims validateToken(String token);

    Long getUserIdFromToken(String token);

}
