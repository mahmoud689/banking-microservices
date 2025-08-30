package com.blackstone.account.dto;

import com.blackstone.account.domain.Account;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AccountDto {
    private String id;
    private String customerId;
    private Long balanceCents;
    private String status;
    private String type;

    public AccountDto(Account account) {
        this.id = account.getId();
        this.customerId = account.getCustomerId();
        this.balanceCents = account.getBalanceCents();
        this.status = account.getStatus();
        this.type = account.getType();
    }
}
