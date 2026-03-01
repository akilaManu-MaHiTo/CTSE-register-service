package com.weatherapp.weatherapp.service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.weatherapp.weatherapp.dto.userDTO.LoginRequestDTO;
import com.weatherapp.weatherapp.dto.userDTO.LoginResponseDTO;
import com.weatherapp.weatherapp.dto.userDTO.RegisterRequestDTO;
import com.weatherapp.weatherapp.dto.userDTO.RegisterResponseDTO;
import com.weatherapp.weatherapp.entity.UserEntity;
import com.weatherapp.weatherapp.repository.UserRepository;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class AuthService {

    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;

    public List<UserEntity> getAllUsers(String userToken) {
        return userRepository.findAll();
    }

    // Create User
    public UserEntity createUser(RegisterRequestDTO userData) {
        UserEntity newUser = new UserEntity();
        newUser.setEmail(userData.getEmail());
        newUser.setPassword(passwordEncoder.encode(userData.getPassword()));

        return userRepository.save(newUser);
    }

    // Register User
    public RegisterResponseDTO register(RegisterRequestDTO req) {

        if (isUserEnableByEmail(req.getEmail()))
            return new RegisterResponseDTO(null, "Email already exists");

        var userData = this.createUser(req);
        if (userData.getId() == null)
            return new RegisterResponseDTO(null, "Error creating user");

        return new RegisterResponseDTO(String.format("User registered at %s", userData.getId()), null);

    }

    // Support function for Register
    private boolean isUserEnableByEmail(String email) {
        return userRepository.existsByEmail(email);
    }

    // Login
    public LoginResponseDTO login(LoginRequestDTO loginData) {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            loginData.getEmail(),
                            loginData.getPassword()));
        } catch (Exception e) {
            return new LoginResponseDTO(
                    null,
                    LocalDateTime.now(),
                    "User not found",
                    "Token not generated");
        }

        var userOptonal = userRepository.findByEmail(loginData.getEmail());
        UserEntity user = userOptonal.get();

        Map<String, Object> claims = new HashMap<String, Object>();
        claims.put("id", user.getId());
        claims.put("email", user.getEmail());

        String token = jwtService.getJWTToken(loginData.getEmail(), claims);

        return new LoginResponseDTO(token, LocalDateTime.now(), null, "Token generated successfully");

    }

    // User response DTO
    public UserEntity getAuthUserDetails(String token) {
        String email = jwtService.getEmail(token);
        Optional<UserEntity> userOptional = userRepository.findByEmail(email);
        if (userOptional.isEmpty()) {
            return null;
        }
        UserEntity user = userOptional.get();
        return user;
    }

}
