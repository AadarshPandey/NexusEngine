package com.nexusengine.core.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@DataJpaTest
class UmsAdminRoleRelationRepositoryTest {

    private final UmsAdminRoleRelationRepository repository;

    @Autowired
    UmsAdminRoleRelationRepositoryTest(UmsAdminRoleRelationRepository repository) {
        this.repository = repository;
    }



    @Test
    void testContextLoads() {
        assertNotNull(repository, "Repository should be autowired");
    }
}
