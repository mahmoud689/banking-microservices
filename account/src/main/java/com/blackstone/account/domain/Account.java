package com.blackstone.account.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "accounts")
public class Account {
    @Id
    @Column(length = 10, nullable = false)
    private String id; // 10 digits, must start with customerId

    @Column(length = 7, nullable = false)
    private String customerId;

    @Column(nullable = false)
    private Long balanceCents;

    @Column(nullable = false)
    private String status; // ACTIVE, CLOSED, SUSPENDED

    @Column(nullable = false)
    private String type;
}
