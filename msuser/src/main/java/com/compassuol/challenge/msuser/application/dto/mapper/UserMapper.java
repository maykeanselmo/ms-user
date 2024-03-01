package com.compassuol.challenge.msuser.application.dto.mapper;

import com.compassuol.challenge.msuser.application.dto.*;
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

    public  static DataNotification toDataNotification(Usuario user){
        return new ModelMapper().map(user, DataNotification.class);
    }

    public  static Usuario toUser(UserLoginDto userLoginDto){
        return new ModelMapper().map(userLoginDto, Usuario.class);
    }

    public  static CepDto toCepDto(Usuario usuario){
        return new ModelMapper().map(usuario, CepDto.class);
    }

    public static UserLoginDto toUserLoginDto(Usuario user){
        return new ModelMapper().map(user, UserLoginDto.class);
    }
    public static UserAddressResponseDto toUserAddressResponseDto(UserResponseDto user, AddressResponseDto address) {
        UserAddressResponseDto dto = new UserAddressResponseDto();
        dto.setUserResponseDto(user);
        dto.setAddress(address);
        return dto;
    }
}
