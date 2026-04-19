package com.example.service.config;

import com.example.service.constant.UrlConstant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
public class SecurityConfig {
    @Autowired
    private JwtAuthenticationFilter jwtFilter;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .cors(cors -> {})
                .csrf(csrf -> csrf.disable())

                .authorizeHttpRequests(auth -> auth
                                // TATIC + HTML
                        .requestMatchers(
                                "/", "/index", "/home",
                                "/register", "/forgot", "/reset_password",
                                "/favicon.ico",
                                "/css/**", "/js/**", "/images/**",
                                "/player.html"
                        ).permitAll()

                                // PUBLIC API
                        .requestMatchers(
                                "/api/login",
                                "/api/register",
                                "/api/login_google",
                                "/api/forgot_password",
                                "/api/reset_password",
                                "/api/update_db_hls",
                                "/profile"
                        ).permitAll()

                        .requestMatchers("/api/**").authenticated()
//                        .anyRequest().authenticated()
                                .anyRequest().permitAll()
                )

                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class)

                .sessionManagement(session ->
                        session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                );

        return http.build();
    }
}
