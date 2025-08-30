package com.blackstone.customer.service;

import com.blackstone.customer.domain.AccountRef;
import com.blackstone.customer.domain.Address;
import com.blackstone.customer.domain.Customer;
import com.blackstone.customer.dto.CustomerRequestDto;
import com.blackstone.customer.repository.CustomerRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
@Transactional
public class CustomerService {
    private final CustomerRepository repo;
    private final WebClient webClient;

    public CustomerService(CustomerRepository repo, WebClient.Builder webClientBuilder) {
        this.repo = repo;
        this.webClient = webClientBuilder.baseUrl("http://localhost:8080").build();
    }

    public Customer create(CustomerRequestDto req) {
        if (repo.existsById(req.getId()))
            throw new IllegalArgumentException("Customer id exists");
        if (repo.existsByLegalId(req.getLegalId()))
            throw new IllegalArgumentException("legalId exists");
        String t = req.getType().toLowerCase();
        if (!List.of("retail", "corporate", "investment").contains(t))
            throw new IllegalArgumentException("invalid type");

        Customer c = new Customer();
        c.setId(req.getId());
        c.setLegalId(req.getLegalId());
        c.setType(req.getType());
        c.setName(req.getName());
        if (req.getAddress() != null) {
            Address a = new Address();
            a.setStreet(req.getAddress().getStreet());
            a.setCity(req.getAddress().getCity());
            a.setState(req.getAddress().getState());
            a.setZipCode(req.getAddress().getZipCode());
            c.setAddress(a);
        }
        if (req.getAccounts() != null && !req.getAccounts().isEmpty()) {
            List<AccountRef> accountRefs = req.getAccounts().stream()
                    .map(dto -> {
                        AccountRef ar = new AccountRef();
                        ar.setAccountId(dto.getAccountId());
                        ar.setType(dto.getType());
                        return ar;
                    })
                    .toList();
            c.setAccounts(accountRefs);
        }
        Customer saved = repo.save(c);

        webClient.post().uri("/api/v1/accounts/events/customer-created")
                .bodyValue(Map.of("customerId", saved.getId(), "type", saved.getType()))
                .retrieve().bodyToMono(Void.class).onErrorResume(e -> Mono.empty()).block();

        return saved;
    }

    public Optional<Customer> findById(String id) {
        return repo.findById(id);
    }
}
