package com.blackstone.customer.repository;

import com.blackstone.customer.domain.Customer;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CustomerRepository extends JpaRepository<Customer, String> {

    Optional<Customer> findByLegalId(String legalId);

    boolean existsByLegalId(String legalId);
}
