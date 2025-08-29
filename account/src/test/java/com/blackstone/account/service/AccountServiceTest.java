package com.blackstone.account.service;

import com.blackstone.account.domain.Account;
import com.blackstone.account.repository.AccountRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Collections;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class AccountServiceTest {
    private AccountRepository repo;
    private WebClient.Builder webClientBuilder;
    private AccountService service;

    private Account account;

    @BeforeEach
    void setUp() {
        repo = Mockito.mock(AccountRepository.class);
        webClientBuilder = WebClient.builder();
        service = new AccountService(repo, webClientBuilder);

        account = new Account();
        account.setId("1234567001");
        account.setCustomerId("1234567");
        account.setType("saving");
        account.setBalanceCents(5000L);
    }

    @Test
    void createAccount_invalidCustomerId_shouldThrow() {
        account.setCustomerId("123"); // invalid length

        assertThatThrownBy(() -> service.create(account))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("invalid customerId");
    }

    @Test
    void createAccount_maxAccountsReached_shouldThrow() {
        when(repo.countByCustomerId("1234567")).thenReturn(10L);

        assertThatThrownBy(() -> service.create(account))
                .isInstanceOf(IllegalStateException.class)
                .hasMessage("max accounts reached");
    }

    @Test
    void createAccount_salaryAccountExists_shouldThrow() {
        account.setType("salary");
        Account existing = new Account();
        existing.setId("1234567002");
        existing.setCustomerId("1234567");
        existing.setType("salary");

        when(repo.findByCustomerId("1234567")).thenReturn(Collections.singletonList(existing));

        assertThatThrownBy(() -> service.create(account))
                .isInstanceOf(IllegalStateException.class)
                .hasMessage("salary account exists");
    }

    @Test
    void createAccount_success() {
        when(repo.countByCustomerId("1234567")).thenReturn(0L);
        when(repo.findByCustomerId("1234567")).thenReturn(Collections.emptyList());
        when(repo.save(any(Account.class))).thenReturn(account);

        Account saved = service.create(account);

        verify(repo, times(1)).save(account);
    }
}
