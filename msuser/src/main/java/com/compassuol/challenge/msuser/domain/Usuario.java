package com.compassuol.challenge.msuser.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.hibernate.validator.constraints.NotEmpty;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;

@Entity
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@Builder
@Table(name = "user")
public class Usuario implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "address_id")
    private Long addressId;
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
    private LocalDate birthdate;
    @Column(name = "email", nullable = false, unique = true)
    @Email(message = "Formato inválido de email")
    @NotEmpty
    private String email;
    @Column(name = "cep", nullable = false)
    @NotEmpty
    @Pattern(regexp = "\\d{5}-\\d{3}", message = "CEP deve seguir o formato xxxxx-xxx")
    private String cep;
    @Column(name = "password", nullable = false)
    @Size(min = 6)
    private String password;
    @Column(name = "active", nullable = false)
    private boolean active;


    public <E> Usuario(String cpf, String password, ArrayList<E> es) {
    }
}
