package com.compassuol.challenge.msuser.application.dto.mapper;

import com.compassuol.challenge.msuser.application.dto.UserCreateDto;
import com.compassuol.challenge.msuser.application.dto.UserResponseDto;
import com.compassuol.challenge.msuser.domain.User;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.modelmapper.ModelMapper;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class UserMapper {
    public static UserResponseDto toDto(User user) {
        return new ModelMapper().map(user, UserResponseDto.class);
    }

    public static User toUser(UserCreateDto dto) {
        return new ModelMapper().map(dto, User.class);
    }
}
