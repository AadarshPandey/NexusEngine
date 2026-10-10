package com.nexusengine.core.domain.payment.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@DataJpaTest
class PaymentTransactionRepositoryTest {
    private final PaymentTransactionRepository repository;

    @Autowired
    PaymentTransactionRepositoryTest(PaymentTransactionRepository repository) {
        this.repository = repository;
    }

    @Test
    void testContextLoads() {
        assertNotNull(repository, "Repository should be autowired");
    }
}
