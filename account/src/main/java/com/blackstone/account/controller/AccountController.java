package com.blackstone.account.controller;

import com.blackstone.account.domain.Account;
import com.blackstone.account.dto.AccountDto;
import com.blackstone.account.dto.AccountRequestDto;
import com.blackstone.account.service.AccountService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/accounts")
public class AccountController {
    private final AccountService accountService;

    public AccountController(AccountService accountService) {
        this.accountService = accountService;
    }

    @PostMapping
    public ResponseEntity<AccountDto> create(@Valid @RequestBody AccountRequestDto dto) {
        Account account = new Account(
                dto.getId(),
                dto.getCustomerId(),
                dto.getBalanceCents(),
                dto.getStatus() != null ? dto.getStatus() : "ACTIVE",
                dto.getType()
        );
        Account created = accountService.create(account);
        return ResponseEntity
                .created(URI.create("/api/v1/accounts/" + created.getId()))
                .body(new AccountDto(created));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Account> findById(@PathVariable("id") String id) {
        return accountService.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

}
