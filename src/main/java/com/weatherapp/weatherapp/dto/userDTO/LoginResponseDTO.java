package com.weatherapp.weatherapp.dto.userDTO;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
public class LoginResponseDTO {

    private String token;
    private LocalDateTime time;
    private String error;
    private String message;

}
