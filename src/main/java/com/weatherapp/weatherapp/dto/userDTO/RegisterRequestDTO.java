package com.weatherapp.weatherapp.dto.userDTO;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
public class RegisterRequestDTO {
    
    private String email;
    private String password;
    
}
