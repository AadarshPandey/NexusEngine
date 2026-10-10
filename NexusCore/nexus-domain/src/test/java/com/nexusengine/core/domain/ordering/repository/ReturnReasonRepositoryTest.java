package com.nexusengine.core.domain.ordering.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@DataJpaTest
class ReturnReasonRepositoryTest {
    private final ReturnReasonRepository repository;

    @Autowired
    ReturnReasonRepositoryTest(ReturnReasonRepository repository) {
        this.repository = repository;
    }

    @Test
    void testContextLoads() {
        assertNotNull(repository, "Repository should be autowired");
    }
}
