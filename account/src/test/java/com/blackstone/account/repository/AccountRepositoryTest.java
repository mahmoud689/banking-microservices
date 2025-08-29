package com.blackstone.account.repository;

import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import com.blackstone.account.domain.Account;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
public class AccountRepositoryTest {
    @Autowired
    private AccountRepository accountRepository;

    @Test
    void testSaveAndFindByCustomerId() {
        Account account = new Account();
        account.setId("1234567001");
        account.setCustomerId("1234567");
        account.setType("saving");
        account.setBalanceCents(5000L);

        accountRepository.save(account);

        List<Account> accounts = accountRepository.findByCustomerId("1234567");

        assertThat(accounts).hasSize(1);
        assertThat(accounts.get(0).getId()).isEqualTo("1234567001");
    }

    @Test
    void testCountByCustomerId() {
        Account account = new Account();
        account.setId("1234567002");
        account.setCustomerId("7654321");
        account.setType("salary");
        account.setBalanceCents(2000L);

        accountRepository.save(account);

        long count = accountRepository.countByCustomerId("7654321");

        assertThat(count).isEqualTo(1);
    }

    @Test
    void testExistsById() {
        Account account = new Account();
        account.setId("1234567999");
        account.setCustomerId("7654321");
        account.setType("investment");
        account.setBalanceCents(20000L);

        accountRepository.save(account);

        boolean exists = accountRepository.existsById("1234567999");

        assertThat(exists).isTrue();
    }
}
