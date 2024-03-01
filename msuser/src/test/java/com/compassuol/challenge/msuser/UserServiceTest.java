package com.compassuol.challenge.msuser;

import com.compassuol.challenge.msuser.application.cosumer.AddressConsumerFeign;
import com.compassuol.challenge.msuser.application.dto.DataNotification;
import com.compassuol.challenge.msuser.application.dto.IdDto;
import com.compassuol.challenge.msuser.application.dto.UserUpdatePasswordDto;
import com.compassuol.challenge.msuser.application.enums.Event;
import com.compassuol.challenge.msuser.application.exceptions.ErrorNotificationException;
import com.compassuol.challenge.msuser.application.exceptions.UniqueUserViolationException;
import com.compassuol.challenge.msuser.application.service.UserService;
import com.compassuol.challenge.msuser.domain.Usuario;
import com.compassuol.challenge.msuser.infra.mqueue.NotificationPublisher;
import com.compassuol.challenge.msuser.infra.repository.UserRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static com.compassuol.challenge.msuser.application.enums.Event.UPDATE_PASSWORD;
import static com.compassuol.challenge.msuser.communs.UserConstatnts.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.hibernate.validator.internal.util.Contracts.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {
    @InjectMocks
    private UserService userService;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private UserRepository userRepository;

    @Mock
    private AddressConsumerFeign addressConsumerFeign;

    @Mock
    private NotificationPublisher notificationPublisher;

    @Test
    public void testCreateUser_WithValidData_ReturnsUser() {

        when(passwordEncoder.encode("password")).thenReturn("encodedPassword");

        IdDto idDto = new IdDto();
        idDto.setId(1L);
        when(addressConsumerFeign.createAddress("12345-678")).thenReturn(ResponseEntity.ok(idDto));

        when(userRepository.save(any(Usuario.class))).thenReturn(VALID_USER);

        Usuario createdUser = userService.createUser(VALID_USER);


        assertNotNull(createdUser);
        assertEquals("encodedPassword", createdUser.getPassword());
        assertEquals(1L, createdUser.getAddressId());

        verify(passwordEncoder).encode("password");
        verify(addressConsumerFeign).createAddress("12345-678");
        verify(userRepository).save(VALID_USER);

    }


    @Test
    public void testCreateUser_WithDuplicateEmailOrPassword_ThrowsUniqueUserViolationException() {

        when(passwordEncoder.encode("password")).thenReturn("encodedPassword");
        when(addressConsumerFeign.createAddress(anyString()))
                .thenReturn(ResponseEntity.ok(new IdDto(1L)));
        when(userRepository.save(any(Usuario.class)))
                .thenThrow(DataIntegrityViolationException.class);

        assertThatThrownBy(() -> userService.createUser(VALID_USER)).isInstanceOf(UniqueUserViolationException.class);

        verify(passwordEncoder).encode("password");
        verify(addressConsumerFeign).createAddress(VALID_USER.getCep());
        verify(userRepository).save(VALID_USER);
    }

    @Test
    public void testCreateUser_JsonProcessingException() {

        when(passwordEncoder.encode("password")).thenReturn("encodedPassword");

        when(addressConsumerFeign.createAddress("12345-678")).thenReturn(ResponseEntity.ok(new IdDto()));

        when(userRepository.save(any(Usuario.class))).thenReturn(new Usuario());

        when(notificationPublisher.createDataNotification(any(Usuario.class), any(Event.class)))
                .thenThrow(new ErrorNotificationException("Erro ao processar a notificação"));

        assertThrows(ErrorNotificationException.class, () -> userService.createUser(VALID_USER));

        verify(passwordEncoder).encode("password");
        verify(addressConsumerFeign).createAddress("12345-678");
        verify(userRepository).save(VALID_USER);
    }

    // corrigir!!!!!!!!!!
    @Test
    public void testCreateUser_WithInvalidData_ThrowsException() {
        when(userRepository.save(INVALID_USER)).thenThrow(RuntimeException.class);

        assertThatThrownBy(() -> userService.createUser(INVALID_USER)).isInstanceOf(RuntimeException.class);

        verify(userRepository, times(1)).save(INVALID_USER);
    }

    @Test
    public void testGetUserById_WithExistingId_ReturnsUser() {
        when(userRepository.findById(2L)).thenReturn(Optional.of(USER_1));
        Usuario sut = userService.getUserById(2L);
        assertThat(sut).isEqualTo(USER_1);
        verify(userRepository, times(1)).findById(2L);
    }

    @Test
    public void getProductById_WithNonExistingId_ThrowsEntityNotFoundException() {
        when(userRepository.findById(2L)).thenReturn(Optional.empty());
        assertThatThrownBy(() -> userService.getUserById(2L)).isInstanceOf(EntityNotFoundException.class);
        verify(userRepository, times(1)).findById(2L);
    }


    @Test
    public void testGetUserByEmail_WithExistingEmail_ReturnsUser() {
        String existingEmail = "jane.smith@example.com";
        when(userRepository.findByEmail(existingEmail)).thenReturn(USER_1);

        Usuario retrievedUser = userService.getUserByEmail(existingEmail);

        assertThat(retrievedUser).isEqualTo(USER_1);
        verify(userRepository, times(1)).findByEmail(existingEmail);
    }

    @Test
    public void testGetUserByEmail_WithNonExistingEmail_ThrowsEntityNotFoundException() {

        String nonExistingEmail = "nonexisting@example.com";
        when(userRepository.findByEmail(nonExistingEmail)).thenReturn(null);

        assertThatThrownBy(() -> userService.getUserByEmail(nonExistingEmail))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessageContaining("Nenhum usuário foi encontrado com o e-mail: " + nonExistingEmail);
        verify(userRepository, times(1)).findByEmail(nonExistingEmail);
    }


    @Test
    public void testUpdateUser_WithExistingId_ReturnsUpdatedUser() {


        when(userRepository.findById(EXISTING_USER.getId())).thenReturn(Optional.of(EXISTING_USER));
        when(userRepository.save(EXISTING_USER)).thenReturn(EXISTING_USER);

        Usuario updatedUser = userService.updateUser(EXISTING_USER.getId(), UPDATING_USER);

        assertNotNull(updatedUser);
        assertEquals(UPDATING_USER.getFirstName(), updatedUser.getFirstName());
        assertEquals(UPDATING_USER.getLastName(), updatedUser.getLastName());
        assertEquals(UPDATING_USER.getEmail(), updatedUser.getEmail());
        assertEquals(UPDATING_USER.getBirthdate(), updatedUser.getBirthdate());
        assertEquals(UPDATING_USER.getCep(), updatedUser.getCep());
        assertEquals(UPDATING_USER.getCpf(), updatedUser.getCpf());
    }

    @Test
    public void testUpdateUser_UsertDoesNotExist_ThrowsEntityNotFoundException() {
        when(userRepository.findById(EXISTING_USER.getId())).thenReturn(Optional.empty());
        assertThatThrownBy(() -> userService.updateUser(EXISTING_USER.getId(), UPDATING_USER))
                .isInstanceOf(EntityNotFoundException.class);

        verify(userRepository, times(1)).findById(EXISTING_USER.getId());
        verify(userRepository, never()).save(any(Usuario.class));
    }

    /// CORRIGIR!
    @Test
    public void testUpdatePassword_Success() throws JsonProcessingException {
        // Define o ID do usuário, a senha atual e a senha atualizada
        Long userId = 1L;
        String currentPassword = "currentPassword";
        UserUpdatePasswordDto updatedPassword = new UserUpdatePasswordDto();
        updatedPassword.setPassword("newPassword");

        // Mock do método getUserById para retornar um usuário existente
        // Mock do método getUserById para retornar um usuário existente
        when(userRepository.findById(userId)).thenReturn(Optional.of(EXISTING_USER));


        // Mock do método matches do passwordEncoder para retornar true
        when(passwordEncoder.matches(currentPassword, EXISTING_USER.getPassword())).thenReturn(true);
        // Mock do encoder

        when(passwordEncoder.encode(anyString())).thenReturn("encodedPassword");


        // Mock do método save do userRepository para retornar o usuário atualizado
        when(userRepository.save(any(Usuario.class))).thenReturn(EXISTING_USER);
        when(notificationPublisher.createDataNotification(EXISTING_USER, UPDATE_PASSWORD)).thenReturn(any(DataNotification.class));



        // Act
        Usuario updatedUser = userService.updatePassword(userId, currentPassword, updatedPassword);

        // Assert
        assertNotNull(updatedUser);
        assertEquals(updatedPassword.getPassword(),"newPassword" );

        // Verificações adicionais
        verify(userRepository, times(1)).findById(userId);
        verify(passwordEncoder, times(1)).matches(currentPassword, EXISTING_USER.getPassword());
        verify(userRepository, times(1)).save(any(Usuario.class));
    }


}




