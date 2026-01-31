package com.adnanumar.projects.airBnbApp.service;

import com.adnanumar.projects.airBnbApp.dto.ProfileUpdateRequestDto;
import com.adnanumar.projects.airBnbApp.dto.UserDto;
import com.adnanumar.projects.airBnbApp.entity.User;

import java.util.List;

public interface UserService {

    User getUserById(Long id);

    void updateProfile(ProfileUpdateRequestDto profileUpdateRequestDto);

    UserDto getMyProfile();

}
