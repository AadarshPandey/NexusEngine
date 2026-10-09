package com.nexusengine.core.repository;

import com.nexusengine.core.model.UmsAdmin;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@Testcontainers
class UmsAdminRepositoryTest {

    private final UmsAdminRepository adminRepository;

    @Autowired
    UmsAdminRepositoryTest(UmsAdminRepository adminRepository) {
        this.adminRepository = adminRepository;
    }


    @Container
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:15-alpine")
            .withDatabaseName("testdb")
            .withUsername("test")
            .withPassword("test");

    @DynamicPropertySource
    static void configureProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgres::getJdbcUrl);
        registry.add("spring.datasource.username", postgres::getUsername);
        registry.add("spring.datasource.password", postgres::getPassword);
        registry.add("spring.jpa.hibernate.ddl-auto", () -> "create-drop");
        registry.add("spring.flyway.enabled", () -> "false");
    }


    @Test
    void saveAndFind() {
        UmsAdmin admin = new UmsAdmin();
        admin.setUsername("testadmin");
        admin.setPassword("encryptedpassword");
        admin.setEmail("test@admin.com");
        admin.setCreateTime(OffsetDateTime.now());
        admin.setStatus(1);

        UmsAdmin saved = adminRepository.save(admin);
        assertNotNull(saved.getId());

        UmsAdmin found = adminRepository.findByUsername("testadmin");
        assertNotNull(found);
        assertEquals("test@admin.com", found.getEmail());
    }
}
