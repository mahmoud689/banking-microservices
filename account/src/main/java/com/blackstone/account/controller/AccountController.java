package com.blackstone.account.controller;

import com.blackstone.account.domain.Account;
import com.blackstone.account.service.AccountService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;

@RestController
@RequestMapping("/api/v1/accounts")
public class AccountController {
    private final AccountService accountService;

    public AccountController(AccountService accountService) {
        this.accountService = accountService;
    }

    @PostMapping
    public ResponseEntity<Account> create(@Valid @RequestBody Account account) {
        Account created = accountService.create(account);
        return ResponseEntity
                .created(URI.create("/api/v1/accounts/" + created.getId()))
                .body(created);
    }
}
