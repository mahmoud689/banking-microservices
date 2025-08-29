package com.blackstone.customer.domain;

import jakarta.persistence.*;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
@Entity
@Table(name = "customers", uniqueConstraints = @UniqueConstraint(columnNames = "legalId"))
public class Customer {
    @Id
    @Column(length = 7, nullable = false)
    private String id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false, unique = true)
    private String legalId;

    @Column(nullable = false)
    private String type; // "retail","corporate","investment"

    @Embedded
    private Address address;

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "customer_id")
    private List<AccountRef> accounts = new ArrayList<>();
}
