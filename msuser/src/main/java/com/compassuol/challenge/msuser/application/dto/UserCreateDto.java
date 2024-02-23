package com.compassuol.challenge.msuser.application.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.Column;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
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
    @Pattern(regexp = "\\d{3}\\.\\d{3}\\.\\d{3}-\\d{2}", message = "CPF deve seguir o padrão xxx.xxx.xxx-xx")
    @NotEmpty
    private String cpf;
    @Column(name = "birthdate", nullable = false)
    @JsonFormat(pattern = "dd-MM-yyyy")
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate birthdate;
    @Column(name = "email", nullable = false, unique = true)
    @Email(message = "Formato inválido de email")
    @NotEmpty
    private String email;
    @Column(name = "cep", nullable = false)
    @Pattern(regexp = "\\d{5}-\\d{3}", message = "CEP deve seguir o formato xxxxx-xxx")
    @NotEmpty
    private String cep;
    @Column(name = "password", nullable = false)
    @Size(min = 6)
    private String password;
    @Column(name = "active", nullable = false)
    private boolean active;
    public boolean getActive() {
        return active;
    }
}
