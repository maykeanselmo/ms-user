package com.compassuol.challenge.msuser.application.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserAddressResponseDto {
   private UserResponseDto userResponseDto;
    private AddressResponseDto address;


}
