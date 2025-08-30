package com.blackstone.account.controller;

import com.blackstone.account.domain.Account;
import com.blackstone.account.dto.AccountRequestDto;
import com.blackstone.account.service.AccountService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(AccountController.class)
public class AccountControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper; // for JSON serialization

    @MockBean
    private AccountService accountService;

    private Account account;

    @BeforeEach
    void setup() {
        account = new Account();
        account.setId("1234567001");       // must start with customerId
        account.setCustomerId("1234567");  // 7 digits
        account.setType("saving");
        account.setBalanceCents(5000L);
    }

    @Test
    void createAccount_success() throws Exception {
        Mockito.when(accountService.create(any(Account.class))).thenReturn(account);

        mockMvc.perform(post("/api/v1/accounts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(account)))
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", "/api/v1/accounts/" + account.getId()))
                .andExpect(jsonPath("$.id").value(account.getId()))
                .andExpect(jsonPath("$.customerId").value(account.getCustomerId()))
                .andExpect(jsonPath("$.type").value("saving"));
    }

    @Test
    void createAccount_invalidCustomerId() throws Exception {
        account.setCustomerId("123"); // invalid length

        Mockito.when(accountService.create(any(Account.class)))
                .thenThrow(new IllegalArgumentException("invalid customerId"));

        mockMvc.perform(post("/api/v1/accounts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(account)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value("invalid customerId"));
    }

}
