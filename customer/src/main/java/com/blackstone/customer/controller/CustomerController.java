package com.blackstone.customer.controller;

import com.blackstone.customer.domain.Customer;
import com.blackstone.customer.dto.CustomerRequestDto;
import com.blackstone.customer.service.CustomerService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;

@RestController
@RequestMapping("/api/v1/customers")
public class CustomerController {

    private final CustomerService customerService;

    public CustomerController(CustomerService customerService) {
        this.customerService = customerService;
    }

    @PostMapping
    public ResponseEntity<Customer> create(@Valid @RequestBody CustomerRequestDto dto) {
        Customer created = customerService.create(dto);
        return ResponseEntity
                .created(URI.create("/api/v1/customers/" + created.getId()))
                .body(created);
    }

}
