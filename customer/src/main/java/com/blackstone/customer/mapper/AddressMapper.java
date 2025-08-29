package com.blackstone.customer.mapper;

import com.blackstone.customer.domain.Address;
import com.blackstone.customer.dto.AddressDto;

public class AddressMapper {
    public static AddressDto toDto(Address address) {
        if (address == null) return null;
        return AddressDto.builder()
                .street(address.getStreet())
                .city(address.getCity())
                .state(address.getState())
                .zipCode(address.getZipCode())
                .build();
    }

    public static Address toEntity(AddressDto dto) {
        if (dto == null) return null;
        Address address = new Address();
        address.setStreet(dto.getStreet());
        address.setCity(dto.getCity());
        address.setState(dto.getState());
        address.setZipCode(dto.getZipCode());
        return address;
    }
}
