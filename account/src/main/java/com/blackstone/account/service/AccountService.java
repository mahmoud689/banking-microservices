package com.blackstone.account.service;

import com.blackstone.account.domain.Account;
import com.blackstone.account.repository.AccountRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import java.util.Map;

@Service
@Transactional
public class AccountService {
    private final AccountRepository repo;
    private final WebClient customerClient; // optional: to get customer type synchronously

    public AccountService(AccountRepository repo, WebClient.Builder wb) {
        this.repo = repo;
        this.customerClient = wb.baseUrl("http://localhost:8081").build();
    }

    public Account create(Account a) {

        if (a.getCustomerId() == null || a.getCustomerId().length() != 7)
            throw new IllegalArgumentException("invalid customerId");
        if (a.getId() == null || a.getId().length() != 10 || !a.getId().startsWith(a.getCustomerId()))
            throw new IllegalArgumentException("invalid account id");

        if (repo.countByCustomerId(a.getCustomerId()) >= 10)
            throw new IllegalStateException("max accounts reached");

        if ("salary".equalsIgnoreCase(a.getType())) {
            var existing = repo.findByCustomerId(a.getCustomerId());
            if (existing.stream().anyMatch(x -> "salary".equalsIgnoreCase(x.getType()))) {
                throw new IllegalStateException("salary account exists");
            }
        }

        if ("investment".equalsIgnoreCase(a.getType()) && (a.getBalanceCents() == null || a.getBalanceCents() < 10_000L)) {
            throw new IllegalArgumentException("investment accounts must have min balance 10000");
        }

        try {
            var cust = customerClient.get().uri("/api/v1/customers/{id}", a.getCustomerId())
                    .retrieve().bodyToMono(Map.class).block();
            if (cust != null && "retail".equalsIgnoreCase((String) cust.get("type")) && !"saving".equalsIgnoreCase(a.getType()))
                throw new IllegalArgumentException("retail customers can only have saving accounts");
        } catch (WebClientResponseException.NotFound ex) {
            throw new IllegalArgumentException("customer not found");
        } catch (Exception e) {
            throw new IllegalStateException("Customer service not reachable, please try again later", e);
        }

        Account saved = repo.save(a);

        return saved;
    }
}
