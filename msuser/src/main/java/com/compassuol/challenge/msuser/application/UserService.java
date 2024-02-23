package com.compassuol.challenge.msuser.application;

import com.compassuol.challenge.msuser.application.dto.UserCreateDto;
import com.compassuol.challenge.msuser.application.dto.UserLoginDto;
import com.compassuol.challenge.msuser.application.dto.UserUpdatePasswordDto;
import com.compassuol.challenge.msuser.application.exceptions.IncorrectPasswordExcpetion;
import com.compassuol.challenge.msuser.application.exceptions.UniqueUserViolationException;
import com.compassuol.challenge.msuser.domain.User;
import com.compassuol.challenge.msuser.infra.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.SQLIntegrityConstraintViolationException;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;

    @Transactional
    public User createUser(User user) {
        try {
            return userRepository.save(user);
        }
        catch (DataIntegrityViolationException e){
            throw new UniqueUserViolationException("Já existe um usuário cadastrado com esse email ou cpf.");

        }

    }

    @Transactional(readOnly = true)
    public User getUserById(Long id) {
        return userRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException("Nenhum usuário foi encontrado com este id: " + id)
        );
    }

    @Transactional
    public User updateUser(Long id, UserCreateDto dto) {
        User existingUser = getUserById(id);
        existingUser.setFirstName(dto.getFirstName());
        existingUser.setLastName(dto.getLastName());
        existingUser.setEmail(dto.getEmail());
        existingUser.setBirthdate(dto.getBirthdate());
        existingUser.setCep(dto.getCep());
        existingUser.setCpf(dto.getCpf());
        existingUser.setActive(dto.getActive());
        return createUser(existingUser);
    }
    @Transactional
    public User updatePassword(Long id, String currentPassword, UserUpdatePasswordDto updatedPassword) {
        User existingUser = getUserById(id);
        if (existingUser.getPassword().equals(currentPassword)) {
            existingUser.setPassword(updatedPassword.getPassword());
            return userRepository.save(existingUser);
        } else
            throw new IncorrectPasswordExcpetion("Senha incorreta");
    }
    @Transactional
    public User login(UserLoginDto dto) {

        User user = userRepository.findByEmail(dto.getEmail());

        if (user == null) {
            throw new EntityNotFoundException("Usuário não encontrado");
        }
        if (!user.getPassword().equals(dto.getPassword())) {
            throw new IncorrectPasswordExcpetion("Senha incorreta.");
        }
        return user;
    }
}
