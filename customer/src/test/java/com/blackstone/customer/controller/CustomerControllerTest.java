package com.blackstone.customer.controller;

import com.blackstone.customer.domain.Address;
import com.blackstone.customer.domain.Customer;
import com.blackstone.customer.dto.AddressDto;
import com.blackstone.customer.dto.CustomerRequestDto;
import com.blackstone.customer.service.CustomerService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(CustomerController.class)
public class CustomerControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper; // for JSON serialization

    @MockBean
    private CustomerService customerService;

    @Test
    void shouldCreateCustomer() throws Exception {
        // given request DTO
        CustomerRequestDto req = new CustomerRequestDto();
        req.setId("C123456");
        req.setName("Alice");
        req.setLegalId("L123");
        req.setType("retail");
        AddressDto address = new AddressDto();
        address.setStreet("123 Main St");
        address.setCity("NYC");
        address.setState("NY");
        address.setZipCode("10001");
        req.setAddress(address);

        // mock service response
        Customer customer = new Customer();
        customer.setId(req.getId());
        customer.setName(req.getName());
        customer.setLegalId(req.getLegalId());
        customer.setType(req.getType());
        Address addr = new Address();
        addr.setStreet(address.getStreet());
        addr.setCity(address.getCity());
        addr.setState(address.getState());
        addr.setZipCode(address.getZipCode());
        customer.setAddress(addr);

        when(customerService.create(any(CustomerRequestDto.class))).thenReturn(customer);

        // when & then
        mockMvc.perform(post("/api/v1/customers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", "/api/v1/customers/" + req.getId()))
                .andExpect(jsonPath("$.id").value(req.getId()))
                .andExpect(jsonPath("$.name").value(req.getName()))
                .andExpect(jsonPath("$.legalId").value(req.getLegalId()))
                .andExpect(jsonPath("$.type").value(req.getType()))
                .andExpect(jsonPath("$.address.city").value("NYC"));
    }

    @Test
    void shouldFailValidationWhenNameMissing() throws Exception {
        CustomerRequestDto req = new CustomerRequestDto();
        req.setId("C123456");
        req.setLegalId("L123");
        req.setType("retail");
        // no name
        req.setAddress(new AddressDto());

        mockMvc.perform(post("/api/v1/customers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isBadRequest());
    }
}
