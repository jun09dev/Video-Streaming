package com.example.service.controller;

import com.example.service.constant.UrlConstant;
import com.example.service.dto.request.UserGoogleLoginRequestDto;
import com.example.service.dto.request.UserLoginRequestDto;
import com.example.service.entity.User;
import com.example.service.service.AuthService;
import com.example.service.service.GoogleAuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
public class AuthController {

    @Autowired
    private AuthService authService;

    @Autowired
    private GoogleAuthService googleAuthService;

    @CrossOrigin("*")
    @PostMapping(UrlConstant.API_LOGIN)
    public Object login(@RequestBody UserLoginRequestDto request) {
        return authService.login(request);
    }

    @PostMapping(UrlConstant.API_LOGIN_GOOGLE)
    public ResponseEntity<?> loginGoogle(@RequestBody UserGoogleLoginRequestDto request) {

        User user = googleAuthService.loginWithGoogle(request.getIdToken());

        Map<String, String> response = new HashMap<>();
        response.put("email", user.getEmail());

        return ResponseEntity.ok(response);
    }

}
