package com.compassuol.challenge.msuser.application.service;

import com.compassuol.challenge.msuser.application.cosumer.AddressConsumerFeign;
import com.compassuol.challenge.msuser.application.dto.*;
import com.compassuol.challenge.msuser.application.enums.Event;
import com.compassuol.challenge.msuser.application.exceptions.ErrorNotificationException;
import com.compassuol.challenge.msuser.application.exceptions.IncorrectPasswordExcpetion;
import com.compassuol.challenge.msuser.application.exceptions.UniqueUserViolationException;
import com.compassuol.challenge.msuser.domain.Usuario;
import com.compassuol.challenge.msuser.infra.mqueue.NotificationPublisher;
import com.compassuol.challenge.msuser.infra.repository.UserRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final NotificationPublisher notificationPublisher;
    private final AddressConsumerFeign addressConsumerFeign;


    public Usuario createUser(Usuario user) {
        try {
            user.setPassword(passwordEncoder.encode(user.getPassword()));

            IdDto idDto = addressConsumerFeign.createAddress(user.getCep()).getBody();
            user.setAddressId(idDto.getId());
            log.info("Endereço criado");
            Usuario userCreated = userRepository.save(user);
            log.info("Usuário salvo");
            DataNotification data = notificationPublisher.createDataNotification(userCreated, Event.CREATE);
            notificationPublisher.publishNotification(data);
            log.info("Mensagem para ms-notification enviada.");
            return userCreated;
        } catch (DataIntegrityViolationException e) {
            throw new UniqueUserViolationException("Já existe um usuário cadastrado com esse email ou cpf.");

        } catch (JsonProcessingException e) {
            throw new ErrorNotificationException("Erro ao processar a notificação");
        }


    }

    @Transactional(readOnly = true)
    public Usuario getUserById(Long id) {
        return userRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException("Nenhum usuário foi encontrado com este id: " + id)
        );
    }

    public Usuario updateUser(Long id, UserCreateDto dto) {
        try {
            Usuario existingUser = getUserById(id);
            existingUser.setFirstName(dto.getFirstName());
            existingUser.setLastName(dto.getLastName());
            existingUser.setEmail(dto.getEmail());
            existingUser.setBirthdate(dto.getBirthdate());
            existingUser.setCep(dto.getCep());
            existingUser.setCpf(dto.getCpf());
            existingUser.setActive(dto.getActive());
            DataNotification data = notificationPublisher.createDataNotification(existingUser, Event.UPDATE);
            notificationPublisher.publishNotification(data);
            log.info("Mensagem para ms-notification enviada");


            return userRepository.save(existingUser);
        } catch (JsonProcessingException e) {
            throw new ErrorNotificationException("Erro ao processar a notificação");
        }

    }


    public Usuario updatePassword(Long id, String currentPassword, UserUpdatePasswordDto updatedPassword) {
        try {
            Usuario existingUser = getUserById(id);
            if (passwordEncoder.matches(currentPassword, existingUser.getPassword())) {
                existingUser.setPassword(passwordEncoder.encode(updatedPassword.getPassword()));
                Usuario userWithPasswordUpdated = userRepository.save(existingUser);
                DataNotification data = notificationPublisher.createDataNotification(userWithPasswordUpdated, Event.UPDATE_PASSWORD);
                notificationPublisher.publishNotification(data);
                return userWithPasswordUpdated;
            } else
                throw new IncorrectPasswordExcpetion("Senha incorreta");
        } catch (JsonProcessingException e) {
            throw new ErrorNotificationException("Erro ao processar a notificação");
        }
    }

    @Transactional(readOnly = true)
    public Usuario getUserByEmail(String email) {

            Usuario user=  userRepository.findByEmail(email);
            if(user == null){
                throw new EntityNotFoundException("Nenhum usuário foi encontrado com o e-mail: " + email);
            }
            else{
                return user;
            }

    }

}
