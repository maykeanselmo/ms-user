package com.compassuol.challenge.msuser.application.cosumer;

import com.compassuol.challenge.msuser.application.dto.AddressResponseDto;
import com.compassuol.challenge.msuser.application.dto.IdDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@FeignClient(value = "address-consumer", url = "localhost:8082",path = "/v1/addresses")
public interface AddressConsumerFeign {
    @PostMapping(value = "/{cep}")
    ResponseEntity<IdDto> createAddress(@PathVariable ("cep")String cep);

    @GetMapping(value = "/{id}")
    ResponseEntity<AddressResponseDto> getAddressById(@PathVariable("id") Long id);
}
