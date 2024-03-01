package com.compassuol.challenge.msuser;

import com.compassuol.challenge.msuser.application.controller.UserController;
import com.compassuol.challenge.msuser.application.dto.UserCreateDto;
import com.compassuol.challenge.msuser.application.dto.UserResponseDto;
import com.compassuol.challenge.msuser.application.service.UserService;
import com.compassuol.challenge.msuser.domain.Usuario;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;

import static com.compassuol.challenge.msuser.communs.UserConstatnts.VALID_USER;
import static org.mockito.ArgumentMatchers.any;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ActiveProfiles("test")
@WebMvcTest(UserController.class)
public class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @MockBean
    private UserService userService;

    private static UserResponseDto toResponseDto(Usuario user) {
        UserResponseDto dto = new UserResponseDto();
        dto.setFirstName(user.getFirstName());
        dto.setLastName(user.getLastName());
        dto.setCep(user.getCep());
        dto.setCpf(user.getCpf());
        dto.setAddressId(user.getAddressId());
        dto.setPassword(user.getPassword());
        dto.setActive(user.isActive());
        dto.setBirthdate(user.getBirthdate());
        dto.setEmail(user.getEmail());

        return dto;
    }
    public static UserCreateDto createUserDto(Usuario user) {
        UserCreateDto userCreateDto = new UserCreateDto();
        userCreateDto.setFirstName(user.getFirstName());
        userCreateDto.setLastName(user.getLastName());
        userCreateDto.setCpf(user.getCpf());
        userCreateDto.setBirthdate(user.getBirthdate());
        userCreateDto.setEmail(user.getEmail());
        userCreateDto.setCep(user.getCep());
        userCreateDto.setPassword(user.getPassword());
        userCreateDto.setActive(user.isActive());
        return userCreateDto;
    }
    @Test
    public void createProduct_WithValidData_ReturnsProduct() throws Exception {
        when(userService.createUser(any(Usuario.class))).thenReturn(VALID_USER);
        final UserResponseDto responseBody = toResponseDto(VALID_USER);

        mockMvc.perform(
                        post("/v1/users")
                                .contentType("application/json")
                                .content(objectMapper.writeValueAsString(createUserDto(VALID_USER)))
                )
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.addressId").value(responseBody.getAddressId()))
                .andExpect(jsonPath("$.firstName").value(responseBody.getFirstName()))
                .andExpect(jsonPath("$.lastName").value(responseBody.getLastName()))
                .andExpect(jsonPath("$.cpf").value(responseBody.getCpf()))
                .andExpect(jsonPath("$.birthdate").value("15-10-1990"))
                .andExpect(jsonPath("$.email").value(responseBody.getEmail()))
                .andExpect(jsonPath("$.cep").value(responseBody.getCep()))
                .andExpect(jsonPath("$.password").value(responseBody.getPassword()))
                .andExpect(jsonPath("$.active").value(responseBody.isActive()));

        verify(userService, times(1)).createUser(any(Usuario.class));
    }
}
