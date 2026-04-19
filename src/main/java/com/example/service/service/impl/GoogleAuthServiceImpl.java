package com.example.service.service.impl;

import com.example.service.dto.request.UserGoogleLoginRequestDto;
import com.example.service.entity.User;
import com.example.service.repository.UserRepository;
import com.example.service.service.GoogleAuthService;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
public class GoogleAuthServiceImpl implements GoogleAuthService {

    private final String CLIENT_ID = "729007110285-4js4oqn73ncitsmeh3l2ou7h2umj490e.apps.googleusercontent.com";

    @Autowired
    private UserRepository userRepository;

    @Override
    public User loginWithGoogle(String idTokenString) {
        try {
            GoogleIdTokenVerifier verifier = new GoogleIdTokenVerifier.Builder(
                    new NetHttpTransport(),
                    new JacksonFactory()
            )
                    .setAudience(Collections.singletonList(CLIENT_ID))
                    .build();

            GoogleIdToken idToken = verifier.verify(idTokenString);
            if (idToken == null) {
                throw new RuntimeException("Invalid Google token");
            }
            GoogleIdToken.Payload payload = idToken.getPayload();
            String email = payload.getEmail();
            String googleId = payload.getSubject();

//            User user = userRepository.findByEmail(email);
            User user = userRepository.findByEmail(email).orElse(null);
//            User user = userRepository.findByEmail(email)
//                    .orElseThrow(() -> new RuntimeException("User not found"));
            if (user == null) {
                user = new User();
                user.setEmail(email);
                user.setProvider("GOOGLE");
                user.setProviderId(googleId);
                userRepository.save(user);
            }
            return user;

        }catch (Exception e){
            throw new RuntimeException("google id token verification failed");
        }
    }
}
