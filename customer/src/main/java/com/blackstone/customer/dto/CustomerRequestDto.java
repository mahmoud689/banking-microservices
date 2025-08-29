package com.blackstone.customer.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class CustomerRequestDto {
    @Pattern(regexp = "\\d{7}", message = "id must be 7 digits")
    private String id;

    @NotBlank
    private String name;

    @NotBlank
    private String legalId;

    @NotBlank
    @Pattern(regexp = "retail|corporate|investment",
            flags = Pattern.Flag.CASE_INSENSITIVE,
            message = "Type must be one of retail, corporate, investment")
    private String type; // valid values checked in service

    @Valid
    private AddressDto address;

    private List<AccountRefDto> accounts;
}
