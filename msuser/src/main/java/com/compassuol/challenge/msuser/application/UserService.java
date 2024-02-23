package com.compassuol.challenge.msuser.application;

import com.compassuol.challenge.msuser.application.dto.UserCreateDto;
import com.compassuol.challenge.msuser.application.dto.UserLoginDto;
import com.compassuol.challenge.msuser.application.dto.UserUpdatePasswordDto;
import com.compassuol.challenge.msuser.application.exceptions.IncorrectPasswordExcpetion;
import com.compassuol.challenge.msuser.application.exceptions.UniqueUserViolationException;
import com.compassuol.challenge.msuser.domain.Usuario;
import com.compassuol.challenge.msuser.infra.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public Usuario createUser(Usuario user) {
        try {
            user.setPassword(passwordEncoder.encode(user.getPassword()));
            return userRepository.save(user);
        }
        catch (DataIntegrityViolationException e){
            throw new UniqueUserViolationException("Já existe um usuário cadastrado com esse email ou cpf.");

        }

    }

    @Transactional(readOnly = true)
    public Usuario getUserById(Long id) {
        return userRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException("Nenhum usuário foi encontrado com este id: " + id)
        );
    }

    @Transactional
    public Usuario updateUser(Long id, UserCreateDto dto) {
        Usuario existingUser = getUserById(id);
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
    public Usuario updatePassword(Long id, String currentPassword, UserUpdatePasswordDto updatedPassword) {
        Usuario existingUser = getUserById(id);
        if (passwordEncoder.matches(currentPassword,existingUser.getPassword())) {
            existingUser.setPassword(passwordEncoder.encode(updatedPassword.getPassword()));
            return userRepository.save(existingUser);
        } else
            throw new IncorrectPasswordExcpetion("Senha incorreta");
    }
    @Transactional
    public Usuario login(UserLoginDto dto) {

        Usuario user = userRepository.findByEmail(dto.getEmail());

        if (user == null) {
            throw new EntityNotFoundException("Usuário não encontrado");
        }
        if (!user.getPassword().equals(dto.getPassword())) {
            throw new IncorrectPasswordExcpetion("Senha incorreta.");
        }
        return user;
    }
    @Transactional(readOnly = true)
    public Usuario getUserByEmail(String email) {
        return  userRepository.findByEmail(email);
    }
    @Transactional(readOnly = true)
    public Usuario findByCpf(String cpf) {
        return userRepository.findByCpf(cpf);

    }
}
