package com.example.service.dto.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserCreatRequestDto {

    private String email;

    private String username;

    private String password;


    public UserCreatRequestDto() {
    }

    public UserCreatRequestDto(String email, String username, String password) {
        this.email = email;
        this.username = username;
        this.password = password;
    }

}
