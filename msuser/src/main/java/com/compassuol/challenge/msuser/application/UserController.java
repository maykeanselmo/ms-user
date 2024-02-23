package com.compassuol.challenge.msuser.application;

import com.compassuol.challenge.msuser.application.dto.UserCreateDto;
import com.compassuol.challenge.msuser.application.dto.UserLoginDto;
import com.compassuol.challenge.msuser.application.dto.UserResponseDto;
import com.compassuol.challenge.msuser.application.dto.UserUpdatePasswordDto;
import com.compassuol.challenge.msuser.application.dto.mapper.UserMapper;
import com.compassuol.challenge.msuser.domain.Usuario;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1")
public class UserController {
    private final UserService userService;

    @PostMapping("/users")
    public ResponseEntity<UserResponseDto> createUser(@Valid @RequestBody UserCreateDto dto) {
        Usuario user = userService.createUser(UserMapper.toUser(dto));
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(UserMapper.toDto(user));
    }

    @GetMapping("/users/{id}")
    public ResponseEntity<UserResponseDto> getUserById(@PathVariable Long id) {
        Usuario user = userService.getUserById(id);
        return ResponseEntity.ok(UserMapper.toDto(user));
    }

    @PutMapping("/users/{id}")
    public ResponseEntity<UserResponseDto> updateUser(@PathVariable Long id, @Valid @RequestBody UserCreateDto dto) {
        Usuario user = userService.updateUser(id, dto);
        return ResponseEntity.ok(UserMapper.toDto(user));
    }

    @PutMapping("/users/{id}/{currentPassword}")
    public ResponseEntity<UserUpdatePasswordDto> updatePassword(@PathVariable Long id, @PathVariable String currentPassword, @Valid @RequestBody UserUpdatePasswordDto updatedPassword) {
        Usuario user = userService.updatePassword(id, currentPassword, updatedPassword);
        return ResponseEntity.ok(UserMapper.toUserUpdatePasswordDto(user));

    }


}
