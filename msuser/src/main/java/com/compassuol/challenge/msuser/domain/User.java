package com.compassuol.challenge.msuser.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.hibernate.validator.constraints.br.CPF;

import java.time.LocalDate;

@Entity
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@Table(name = "user")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "first_name", nullable = false)
    @Size(min = 3)
    private String firstName;
    @Column(name = "last_name", nullable = false)
    @Size(min = 3)
    private String lastName;
    @Column(name = "cpf", nullable = false, unique = true)
    @Pattern(regexp = "\\d{3}-\\d{3}-\\d{3}\\.\\d{2}", message = "CPF deve seguir o padrão xxx-xxx-xxx.xx")
    private String cpf;
    @Column(name = "birthdate", nullable = false)
    private LocalDate birthdate;
    @Column(name = "email", nullable = false, unique = true)
    @Email(message = "Formato inválido de email")
    private String email;
    @Column(name = "cep",nullable = false)
    private String cep;
    @Column(name = "password", nullable = false)
    @Size(min = 6)
    private String password;
    @Column(name = "active", nullable = false)
    private boolean active;


}
