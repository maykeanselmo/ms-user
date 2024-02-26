package com.compassuol.challenge.msuser.application.controller;

import com.compassuol.challenge.msuser.application.dto.UserCreateDto;
import com.compassuol.challenge.msuser.application.dto.UserResponseDto;
import com.compassuol.challenge.msuser.application.dto.UserUpdatePasswordDto;
import com.compassuol.challenge.msuser.application.dto.mapper.UserMapper;
import com.compassuol.challenge.msuser.application.exceptions.handler.ErrorMessage;
import com.compassuol.challenge.msuser.application.service.UserService;
import com.compassuol.challenge.msuser.domain.Usuario;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Microserviço de usuário", description = "Contém as operações relativas ao domínio de usuário," +
        "incluindo criação, leitura e atualização de usuários.")
@RestController
@RequiredArgsConstructor
@RequestMapping("/v1")
public class UserController {
    private final UserService userService;

    @Operation(summary = "Criar um novo usuário.", description = "Recurso para criar um novo usuário.",
            responses = {
                    @ApiResponse(responseCode = "201", description = "Usuário criado com sucesso.",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = UserResponseDto.class))
                    ),
                    @ApiResponse(responseCode = "400", description = "Requisição inválida.",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorMessage.class))
                    ),
                    @ApiResponse(responseCode = "409", description = "Já existe um usuário cadastrado com esse email ou cpf.",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorMessage.class))
                    )
            }
    )
    @PostMapping("/users")
    public ResponseEntity<UserResponseDto> createUser(@Valid @RequestBody UserCreateDto dto) {
        Usuario user = userService.createUser(UserMapper.toUser(dto));
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(UserMapper.toDto(user));
    }

    @Operation(summary = "Recuperar informações de um usuário existente.", description = "Recurso para recuperar um usuário existente através do Id.",
            parameters = {
                    @Parameter(name = "id", description = "Identificador (Id) do usuário no banco de dados.",
                            in = ParameterIn.PATH, required = true)
            },
            responses = {
                    @ApiResponse(responseCode = "200", description = "Usuário encontrado com sucesso",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = UserResponseDto.class))),
                    @ApiResponse(responseCode = "404", description = "Usuário não encontrado",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorMessage.class))),
                    @ApiResponse(responseCode = "401", description = "Não autorizado.",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorMessage.class))
                    )
            }
    )
    @GetMapping("/users/{id}")
    public ResponseEntity<UserResponseDto> getUserById(@PathVariable Long id) {
        Usuario user = userService.getUserById(id);
        return ResponseEntity.ok(UserMapper.toDto(user));
    }

    @Operation(summary = "Atualizar um usuário existente.", description = "Recurso para atualizar as informações de um usuário existente através do Id.",
            parameters = {
                    @Parameter(name = "id", description = "Identificador (Id) do usuário no banco de dados.",
                            in = ParameterIn.PATH, required = true)
            },
            responses = {
                    @ApiResponse(responseCode = "200", description = "Usuário atualizado com sucesso.",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = UserResponseDto.class))
                    ),
                    @ApiResponse(responseCode = "400", description = "Requisição inválida.",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorMessage.class))
                    ),
                    @ApiResponse(responseCode = "404", description = "Usuário não encontrado.",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorMessage.class))
                    ),
                    @ApiResponse(responseCode = "401", description = "Não autorizado.",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorMessage.class))
                    )
            }
    )
    @PutMapping("/users/{id}")
    public ResponseEntity<UserResponseDto> updateUser(@PathVariable Long id, @Valid @RequestBody UserCreateDto dto) {
        Usuario user = userService.updateUser(id, dto);
        return ResponseEntity.ok(UserMapper.toDto(user));
    }

    @Operation(summary = "Atualizar a senha do usuário.", description = "Recurso para atualizar a senha de um usuário existente através do Id e a confirmação da senha atual.",
            parameters = {
                    @Parameter(name = "id", description = "Identificador (Id) do usuário no banco de dados.",
                            in = ParameterIn.PATH, required = true),
                    @Parameter(name = "currentPassword", description = "Senha atual do usuário",
                            in = ParameterIn.PATH, required = true)
            },
            responses = {
                    @ApiResponse(responseCode = "200", description = "Senha atualizada com sucesso.",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = UserUpdatePasswordDto.class))
                    ),
                    @ApiResponse(responseCode = "400", description = "Requisição inválida.",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorMessage.class))
                    ),
                    @ApiResponse(responseCode = "404", description = "Usuário não encontrado.",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorMessage.class))
                    ),
                    @ApiResponse(responseCode = "401", description = "Não autorizado.",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorMessage.class))
                    )
            }
    )

    @PutMapping("/users/{id}/{currentPassword}")
    public ResponseEntity<UserUpdatePasswordDto> updatePassword(@PathVariable Long id, @PathVariable String currentPassword, @Valid @RequestBody UserUpdatePasswordDto updatedPassword) {
        Usuario user = userService.updatePassword(id, currentPassword, updatedPassword);
        return ResponseEntity.ok(UserMapper.toUserUpdatePasswordDto(user));

    }


}
