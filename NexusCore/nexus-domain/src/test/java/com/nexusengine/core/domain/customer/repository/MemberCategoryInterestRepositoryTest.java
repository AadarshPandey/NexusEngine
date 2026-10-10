package com.nexusengine.core.domain.customer.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@DataJpaTest
class MemberCategoryInterestRepositoryTest {

    private final MemberCategoryInterestRepository repository;

    @Autowired
    MemberCategoryInterestRepositoryTest(MemberCategoryInterestRepository repository) {
        this.repository = repository;
    }



    @Test
    void testContextLoads() {
        assertNotNull(repository, "Repository should be autowired");
    }
}
