package com.weatherapp.weatherapp.service;

import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import com.weatherapp.weatherapp.entity.UserEntity;
import com.weatherapp.weatherapp.repository.UserRepository;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class UserDetailService implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        UserEntity userData = userRepository.findByEmail(email).orElse(null);
        if (userData == null) {
            throw new UsernameNotFoundException("User not found");
        }
        UserDetails user = User.builder()
                .username(userData.getEmail())
                .password(userData.getPassword())
                .build();
        return user;
    }
    
}
