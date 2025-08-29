package com.blackstone.customer.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Data
@Entity
@Table(name = "account_refs")
public class AccountRef {
    @Id
    @Column(length = 10)
    private String accountId;

    @Column
    private String type;
}
