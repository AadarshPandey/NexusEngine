package com.nexusengine.core.domain.customer.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@DataJpaTest
class MemberRepositoryTest {

    private final MemberRepository repository;

    @Autowired
    MemberRepositoryTest(MemberRepository repository) {
        this.repository = repository;
    }



    @Test
    void testContextLoads() {
        assertNotNull(repository, "Repository should be autowired");
    }
}
