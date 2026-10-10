package com.nexusengine.core.domain.iam.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@DataJpaTest
class RoleMenuRepositoryTest {

    private final RoleMenuRepository repository;

    @Autowired
    RoleMenuRepositoryTest(RoleMenuRepository repository) {
        this.repository = repository;
    }



    @Test
    void testContextLoads() {
        assertNotNull(repository, "Repository should be autowired");
    }
}
