package com.blackstone.account.repository;

import com.blackstone.account.domain.Account;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AccountRepository extends JpaRepository<Account, String> {

    List<Account> findByCustomerId(String customerId);

    long countByCustomerId(String customerId);

    boolean existsById(String id);
}
