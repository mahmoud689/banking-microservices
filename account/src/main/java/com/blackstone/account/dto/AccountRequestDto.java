package com.blackstone.account.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AccountRequestDto {
    @Pattern(regexp = "\\d{10}", message = "Account ID must be 10 digits")
    private String id;

    @Pattern(regexp = "\\d{7}", message = "Customer ID must be 7 digits")
    private String customerId;

    @NotNull(message = "Balance is required")
    @Min(value = 0, message = "Balance must be >= 0")
    private Long balanceCents;

    @NotBlank(message = "Status is required")
    @Pattern(regexp = "ACTIVE|CLOSED|SUSPENDED", message = "Status must be ACTIVE, CLOSED or SUSPENDED")
    private String status;

    @NotBlank(message = "Type is required")
    private String type; // validated further in AccountService
}
