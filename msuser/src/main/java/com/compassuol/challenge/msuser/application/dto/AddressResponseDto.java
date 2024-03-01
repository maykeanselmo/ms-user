package com.compassuol.challenge.msuser.application.dto;

import lombok.*;

@Getter
@Setter
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
public class AddressResponseDto {
    private String cep;
    private String logradouro;
    private String complemento;
    private String localidade;
    private String uf;
}
