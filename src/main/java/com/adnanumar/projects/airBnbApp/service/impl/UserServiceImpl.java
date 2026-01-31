package com.adnanumar.projects.airBnbApp.service.impl;

import com.adnanumar.projects.airBnbApp.dto.ProfileUpdateRequestDto;
import com.adnanumar.projects.airBnbApp.dto.UserDto;
import com.adnanumar.projects.airBnbApp.entity.User;
import com.adnanumar.projects.airBnbApp.exception.ResourceNotFoundException;
import com.adnanumar.projects.airBnbApp.repository.UserRepository;
import com.adnanumar.projects.airBnbApp.service.UserService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.adnanumar.projects.airBnbApp.util.AppUtils.getCurrentUser;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Slf4j
public class UserServiceImpl implements UserService, UserDetailsService {

    final UserRepository userRepository;

    final ModelMapper modelMapper;

    @Override
    public User getUserById(Long id) {
        return userRepository.findById(id).orElseThrow(() ->
                new ResourceNotFoundException("User not found with id: " + id));
    }

    @Override
    public void updateProfile(ProfileUpdateRequestDto profileUpdateRequestDto) {
        log.info("Updating user profile");
        User user = getCurrentUser();

        if (profileUpdateRequestDto.getDateOfBirth() != null) {
            user.setDateOfBirth(profileUpdateRequestDto.getDateOfBirth());
        }

        if (profileUpdateRequestDto.getGender() != null) {
            user.setGender(profileUpdateRequestDto.getGender());
        }

        if (profileUpdateRequestDto.getName() != null) {
            user.setName(profileUpdateRequestDto.getName());
        }

        userRepository.save(user);
    }

    @Override
    public UserDto getMyProfile() {
        log.info("Getting the profile for user with id {}", getCurrentUser().getId());
        return modelMapper.map(getCurrentUser(), UserDto.class);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByEmail(username).orElse(null);
    }

}
