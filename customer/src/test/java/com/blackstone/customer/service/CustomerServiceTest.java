package com.blackstone.customer.service;

import com.blackstone.customer.domain.Address;
import com.blackstone.customer.domain.Customer;
import com.blackstone.customer.dto.AccountRefDto;
import com.blackstone.customer.dto.AddressDto;
import com.blackstone.customer.dto.CustomerRequestDto;
import com.blackstone.customer.repository.CustomerRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class CustomerServiceTest {
    @Mock
    private CustomerRepository repo;

    @Mock
    private WebClient webClient;

    @Mock
    private WebClient.RequestBodyUriSpec uriSpec;
    @Mock
    private WebClient.RequestBodySpec bodySpec;
    @Mock
    private WebClient.RequestHeadersSpec headersSpec;
    @Mock
    private WebClient.ResponseSpec responseSpec;

    @InjectMocks
    private CustomerService service;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void createCustomer_successful() {
        List<AccountRefDto> accounts = new ArrayList<>();
        AddressDto addressDto = new AddressDto("Main St", "Springfield", "IL", "62704");
        CustomerRequestDto dto = new CustomerRequestDto("C123456", "John Doe", "L12345", "retail", addressDto, accounts);

        when(repo.existsById(dto.getId())).thenReturn(false);
        when(repo.existsByLegalId(dto.getLegalId())).thenReturn(false);

        Customer saved = new Customer();
        saved.setId(dto.getId());
        saved.setName(dto.getName());
        saved.setLegalId(dto.getLegalId());
        saved.setType(dto.getType());
        saved.setAddress(new Address());

        when(repo.save(any(Customer.class))).thenReturn(saved);

        // Mock WebClient chain
        when(webClient.post()).thenReturn(uriSpec);
        when(uriSpec.uri(anyString())).thenReturn(bodySpec);
        when(bodySpec.bodyValue(any(Map.class))).thenReturn(headersSpec);
        when(headersSpec.retrieve()).thenReturn(responseSpec);
        when(responseSpec.bodyToMono(Void.class)).thenReturn(Mono.empty());

        // Act
        Customer result = service.create(dto);

        // Assert
        assertNotNull(result);
        assertEquals("C123456", result.getId());
        verify(repo).save(any(Customer.class));
        verify(webClient).post();
    }

    @Test
    void createCustomer_duplicateId_throws() {
        CustomerRequestDto dto = new CustomerRequestDto("C123456", "John Doe", "L12345", "retail", null, null);
        when(repo.existsById(dto.getId())).thenReturn(true);

        assertThrows(IllegalArgumentException.class, () -> service.create(dto));
    }

    @Test
    void createCustomer_invalidType_throws() {
        CustomerRequestDto dto = new CustomerRequestDto("C123456", "John Doe", "L12345", "wrongType", null, null);
        when(repo.existsById(dto.getId())).thenReturn(false);
        when(repo.existsByLegalId(dto.getLegalId())).thenReturn(false);

        assertThrows(IllegalArgumentException.class, () -> service.create(dto));
    }
}
