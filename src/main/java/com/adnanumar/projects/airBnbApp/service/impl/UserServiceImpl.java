package com.adnanumar.projects.airBnbApp.service.impl;

import com.adnanumar.projects.airBnbApp.entity.User;
import com.adnanumar.projects.airBnbApp.exception.ResourceNotFoundException;
import com.adnanumar.projects.airBnbApp.repository.UserRepository;
import com.adnanumar.projects.airBnbApp.service.UserService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserServiceImpl implements UserService, UserDetailsService {

    final UserRepository userRepository;

    @Override
    public User getUserById(Long id) {
        return userRepository.findById(id).orElseThrow(() ->
                new ResourceNotFoundException("User not found with id: " + id));
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByEmail(username).orElse(null);
    }

}
