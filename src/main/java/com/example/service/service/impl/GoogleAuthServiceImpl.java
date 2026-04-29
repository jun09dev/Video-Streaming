package com.example.service.service.impl;

import com.example.service.entity.User;
import com.example.service.repository.UserRepository;
import com.example.service.service.GoogleAuthService;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.gson.GsonFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;

@Service
public class GoogleAuthServiceImpl implements GoogleAuthService {

    // 🔥 Cho phép nhiều client (web + mobile nếu có)
    private final String[] CLIENT_IDS = {
            "729007110285-4js4oqn73ncitsmeh3l2ou7h2umj490e.apps.googleusercontent.com",
            "904543315574-3fnld2pil11ifai0r3v5fk5jfb5lpcom.apps.googleusercontent.com",
            // thêm web nếu cần
            // "WEB_CLIENT_ID"
    };

    @Autowired
    private UserRepository userRepository;

    @Override
    public User loginWithGoogle(String idTokenString) {
        try {
            System.out.println("ID TOKEN: " + idTokenString);

            GoogleIdTokenVerifier verifier = new GoogleIdTokenVerifier.Builder(
                    new NetHttpTransport(),
                    GsonFactory.getDefaultInstance()
            )
                    .setAudience(Arrays.asList(CLIENT_IDS))
                    .build();

            GoogleIdToken idToken = verifier.verify(idTokenString);

            if (idToken == null) {
                throw new RuntimeException("Invalid Google token (verify returned null)");
            }

            GoogleIdToken.Payload payload = idToken.getPayload();

            // 🔥 Check issuer (bắt buộc)
            String issuer = payload.getIssuer();
            if (!"accounts.google.com".equals(issuer) &&
                    !"https://accounts.google.com".equals(issuer)) {
                throw new RuntimeException("Invalid issuer: " + issuer);
            }

            String email = payload.getEmail();
            String googleId = payload.getSubject();

            System.out.println("EMAIL: " + email);

            User user = userRepository.findByEmail(email).orElse(null);

            if (user == null) {
                user = new User();
                user.setEmail(email);
                user.setProvider("GOOGLE");
                user.setProviderId(googleId);
                userRepository.save(user);
            }

            return user;

        } catch (Exception e) {
            e.printStackTrace(); // 🔥 giữ lại log thật
            throw new RuntimeException("Google verify failed: " + e.getMessage());
        }
    }
}