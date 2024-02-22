package com.compassuol.challenge.msuser.application.dto;

import jakarta.persistence.Column;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import org.hibernate.validator.constraints.NotEmpty;

import java.time.LocalDate;

public class UserCreateDto {
    @NotEmpty
    @Column(name = "first_name", nullable = false)
    @Size(min = 3)
    private String firstName;
    @NotEmpty
    @Column(name = "last_name", nullable = false)
    @Size(min = 3)
    private String lastName;
    @NotEmpty
    @Column(name = "cpf", nullable = false, unique = true)
    @Pattern(regexp = "\\d{3}-\\d{3}-\\d{3}\\.\\d{2}", message = "CPF deve seguir o padrão xxx-xxx-xxx.xx")
    @NotEmpty
    private String cpf;
    @Column(name = "birthdate", nullable = false)
    private LocalDate birthdate;
    @Column(name = "email", nullable = false, unique = true)
    @Email(message = "Formato inválido de email")
    @NotEmpty
    private String email;
    @Column(name = "cep", nullable = false)
    @NotEmpty
    private String cep;
    @Column(name = "password", nullable = false)
    @Size(min = 6)
    private String password;
    @Column(name = "active", nullable = false)
    private boolean active;
}
