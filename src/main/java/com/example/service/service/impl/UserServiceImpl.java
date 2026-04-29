package com.example.service.service.impl;

import com.example.service.dto.request.UserCreatRequestDto;
import com.example.service.dto.response.UserCreatResponseDto;
import com.example.service.dto.response.UserProfileResponseDto;
import com.example.service.dto.response.UserResponseDto;
import com.example.service.entity.User;
import com.example.service.repository.UserRepository;
import com.example.service.service.EmailService;
import com.example.service.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private EmailService  emailService;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @Override
    public UserCreatResponseDto createUser(UserCreatRequestDto req) {
//        find user not null
//        User userExit = userRepository.findByEmail(req.getEmail());
//        User userExit = userRepository.findByEmail(req.getEmail())
//                .orElseThrow(() -> new RuntimeException("User not found"));
//        if (userExit != null) {
//            throw new RuntimeException("User already exists");
//        }

        Optional<User> existingUser = userRepository.findByEmail(req.getEmail());

        if (existingUser.isPresent()) {
            throw new RuntimeException("Email already exists");
        }

//        creat user
        User UserCreat = new User();
        UserCreat.setEmail(req.getEmail());
        UserCreat.setPassword(passwordEncoder.encode(req.getPassword()));
        UserCreat.setUsername(req.getUsername());
        UserCreat.setProvider("local");

        userRepository.save(UserCreat);

        // 👉 gửi mail welcome
        emailService.sendWelcomeEmail(
                UserCreat.getEmail(),
                UserCreat.getUsername()
        );
//        connection DB

//        return response
        UserCreatResponseDto userRes = new UserCreatResponseDto();
        userRes.setEmail(UserCreat.getEmail());
        return userRes;
    }

    @Override
    public UserResponseDto updateUsername(String email, String newUsername) {

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        // validate
        if (userRepository.existsByUsername(newUsername)) {
            throw new RuntimeException("Username already exists");
        }

        if (newUsername == null || newUsername.length() < 3) {
            throw new RuntimeException("Username invalid");
        }

        user.setUsername(newUsername);
        userRepository.save(user);

        return new UserResponseDto(
                user.getId(),
                user.getUsername(),
                user.getEmail()
        );
    }
}
