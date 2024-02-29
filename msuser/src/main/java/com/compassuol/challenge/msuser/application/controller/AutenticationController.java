package com.compassuol.challenge.msuser.application.controller;

import com.compassuol.challenge.msuser.application.dto.DataNotification;
import com.compassuol.challenge.msuser.application.dto.UserLoginDto;
import com.compassuol.challenge.msuser.application.dto.mapper.UserMapper;
import com.compassuol.challenge.msuser.application.enums.Event;
import com.compassuol.challenge.msuser.application.exceptions.ErrorNotificationException;
import com.compassuol.challenge.msuser.application.exceptions.handler.ErrorMessage;
import com.compassuol.challenge.msuser.infra.mqueue.NotificationPublisher;
import com.compassuol.challenge.msuser.jwt.JwtToken;
import com.compassuol.challenge.msuser.jwt.JwtUserDetailsService;
import com.fasterxml.jackson.core.JsonProcessingException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Controlador de Autenticação", description = "Controlador responsável por gerenciar as operações de autenticação, como login de usuário.")
@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/v1")
public class AutenticationController {
    private final JwtUserDetailsService detailsService;
    private final AuthenticationManager authenticationManager;
    private final NotificationPublisher notificationPublisher;

    @Operation(summary = "Autenticar na API", description = "Recurso de autenticação na API",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Autenticação realizada com sucesso e retorno de um bearer token",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = UserLoginDto.class))),
                    @ApiResponse(responseCode = "400", description = "Credenciais inválidas",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorMessage.class))),
                    @ApiResponse(responseCode = "422", description = "Campo(s) Inválido(s)",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorMessage.class)))
            })
    @PostMapping("/login")
    public ResponseEntity<?> autenticar(@RequestBody @Valid UserLoginDto dto, HttpServletRequest request) {
        log.info("Processo de autenticação pelo login {}", dto.getEmail());
        try {
            UsernamePasswordAuthenticationToken authenticationToken =
                    new UsernamePasswordAuthenticationToken(dto.getEmail(), dto.getPassword());

            authenticationManager.authenticate(authenticationToken);

            JwtToken token = detailsService.getTokenAuthenticated(dto.getEmail());
            DataNotification data = notificationPublisher.createDataNotification(UserMapper.toUser(dto), Event.LOGIN);
            notificationPublisher.publishNotification(data);
            log.info("Login efetuado com sucesso");


            return ResponseEntity.ok(token);
        } catch (AuthenticationException ex) {
            log.warn("Bad Credentials from username '{}'", dto.getEmail());
        }
        catch (JsonProcessingException e) {
            throw new ErrorNotificationException("Erro ao processar a notificação");
        }
        return ResponseEntity
                .badRequest()
                .body(new ErrorMessage(request, HttpStatus.BAD_REQUEST, "Credenciais Inválidas"));
    }
}
