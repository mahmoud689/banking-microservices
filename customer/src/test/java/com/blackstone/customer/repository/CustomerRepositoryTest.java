package com.blackstone.customer.repository;

import com.blackstone.customer.domain.Customer;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@DataJpaTest
public class CustomerRepositoryTest {
    @Autowired
    private CustomerRepository customerRepository;

    @Test
    @DisplayName("should save and find by legalId")
    void testFindByLegalId() {
        Customer c = new Customer();
        c.setId("CUST001");
        c.setName("Alice");
        c.setLegalId("LID-123");
        c.setType("retail");

        customerRepository.save(c);

        Optional<Customer> found = customerRepository.findByLegalId("LID-123");

        assertThat(found).isPresent();
        assertThat(found.get().getName()).isEqualTo("Alice");
    }

    @Test
    @DisplayName("should return true when legalId exists")
    void testExistsByLegalId() {
        Customer c = new Customer();
        c.setId("CUST002");
        c.setName("Bob");
        c.setLegalId("LID-456");
        c.setType("corporate");

        customerRepository.save(c);

        boolean exists = customerRepository.existsByLegalId("LID-456");

        assertThat(exists).isTrue();
    }

    @Test
    @DisplayName("should return false when legalId does not exist")
    void testExistsByLegalIdFalse() {
        boolean exists = customerRepository.existsByLegalId("UNKNOWN");
        assertThat(exists).isFalse();
    }
}
