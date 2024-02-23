package com.compassuol.challenge.msuser.application.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@ToString
@AllArgsConstructor
public class UserLoginDto {
    @Email(message = "Formato inválido de email")
    private String email;
    @Size(min = 6)
    private String password;

}
