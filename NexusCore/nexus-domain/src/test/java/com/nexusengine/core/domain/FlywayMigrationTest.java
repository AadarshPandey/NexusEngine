package com.nexusengine.core.domain;

import static org.junit.jupiter.api.Assertions.assertTrue;

import javax.sql.DataSource;
import org.flywaydb.core.Flyway;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

@Tag("docker")
@DataJpaTest
@Testcontainers(disabledWithoutDocker = true)
class FlywayMigrationTest {

    @Container
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("pgvector/pgvector:pg17")
            .withDatabaseName("nexuscore_test")
            .withUsername("test")
            .withPassword("test");

    @DynamicPropertySource
    static void configureProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgres::getJdbcUrl);
        registry.add("spring.datasource.username", postgres::getUsername);
        registry.add("spring.datasource.password", postgres::getPassword);
        registry.add("spring.jpa.hibernate.ddl-auto", () -> "validate");
        registry.add("spring.flyway.enabled", () -> "true");
        registry.add("spring.flyway.schemas", () -> "catalog,ordering,customer,iam,promotion,engagement,payment,messaging");
    }

    private final DataSource dataSource;

    @Autowired
    FlywayMigrationTest(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Test
    void testFlywayMigrations() throws Exception {
        Flyway flyway = Flyway.configure()
                .dataSource(dataSource)
                .locations("classpath:db/migration")
                .schemas("catalog", "ordering", "customer", "iam", "promotion", "engagement", "payment", "messaging")
                .load();

        flyway.migrate();

        assertTrue(flyway.info().applied().length >= 3, "Flyway migrations (V1, V2, V3) should have been applied");

        try (var conn = dataSource.getConnection();
                var stmt = conn.createStatement()) {
            try (var rs = stmt.executeQuery("SELECT COUNT(*) FROM iam.role")) {
                assertTrue(rs.next() && rs.getInt(1) >= 1, "Reference roles should be seeded");
            }
            try (var rs = stmt.executeQuery("SELECT COUNT(*) FROM iam.admin_user")) {
                assertTrue(rs.next() && rs.getInt(1) >= 1, "Admin users should be seeded");
            }
            try (var rs = stmt.executeQuery("SELECT COUNT(*) FROM ordering.order_policy")) {
                assertTrue(rs.next() && rs.getInt(1) >= 1, "Order policy row should be seeded");
            }
            try (var rs = stmt.executeQuery("SELECT COUNT(*) FROM catalog.product")) {
                assertTrue(rs.next() && rs.getInt(1) >= 1, "Demo catalog products should be seeded");
            }
        }
    }
}
