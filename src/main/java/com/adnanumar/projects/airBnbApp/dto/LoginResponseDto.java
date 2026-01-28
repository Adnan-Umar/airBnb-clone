package com.adnanumar.projects.airBnbApp.dto;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class LoginResponseDto {

    String token;

    public LoginResponseDto(String token) {
        this.token = token;
    }
}
