package com.compassuol.challenge.msuser.communs;

import com.compassuol.challenge.msuser.application.dto.UserCreateDto;
import com.compassuol.challenge.msuser.domain.Usuario;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class UserConstatnts {
    public static final Usuario VALID_USER = new Usuario(1L,1L,"John","Doe","123.456.789-00",LocalDate.of(1990, 1, 1),"john.doe@example.com","12345-678","password",true);

    public static final Usuario INVALID_USER = new Usuario(2L,2L,"Ana","Julia","cpfInvalido",LocalDate.of(1990, 1, 1),"emailInvalido","CepInvalido","123456789",true);

    public static final Usuario USER_1 = new Usuario(2L, 2L, "Jane", "Smith", "987.654.321-00", LocalDate.of(1995, 5, 15), "jane.smith@example.com", "54321-876", "password123", true
    );
    public static final Usuario EXISTING_USER = new Usuario(3L, 3L, "Michael", "Johnson", "111.222.333-44", LocalDate.of(1985, 9, 20), "michael.johnson@example.com", "98765-432", "pass123", true
    );
    public static final UserCreateDto UPDATING_USER = new UserCreateDto("Michael","Johnson","111.222.333-44",LocalDate.of(1985, 9, 20),"michael.johnson@example.com","98765-432","password",true);

}
