package com.compassuol.challenge.msuser.application.dto.mapper;

import com.compassuol.challenge.msuser.application.dto.UserCreateDto;
import com.compassuol.challenge.msuser.application.dto.UserLoginDto;
import com.compassuol.challenge.msuser.application.dto.UserResponseDto;
import com.compassuol.challenge.msuser.application.dto.UserUpdatePasswordDto;
import com.compassuol.challenge.msuser.domain.Usuario;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.modelmapper.ModelMapper;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class UserMapper {
    public static UserResponseDto toDto(Usuario user) {
        return new ModelMapper().map(user, UserResponseDto.class);
    }

    public static Usuario toUser(UserCreateDto dto) {
        return new ModelMapper().map(dto, Usuario.class);
    }
    public  static UserUpdatePasswordDto toUserUpdatePasswordDto(Usuario user){
        return new ModelMapper().map(user, UserUpdatePasswordDto.class);
    }

    public static UserLoginDto toUserLoginDto(Usuario user){
        return new ModelMapper().map(user, UserLoginDto.class);
    }
}
