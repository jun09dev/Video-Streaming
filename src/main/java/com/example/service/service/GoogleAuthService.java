package com.example.service.service;

import com.example.service.entity.User;

public interface GoogleAuthService {
    User loginWithGoogle(String idToken);
}
