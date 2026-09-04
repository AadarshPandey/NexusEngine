package com.nexusengine.core.portal.integration;

import com.nexusengine.core.portal.service.impl.*;
import com.nexusengine.core.portal.controller.*;
import com.nexusengine.core.model.PmsProduct;
import com.nexusengine.core.portal.service.impl.*;
import com.nexusengine.core.portal.controller.*;
import com.nexusengine.core.model.PmsProductEmbedding;
import com.nexusengine.core.portal.service.impl.*;
import com.nexusengine.core.portal.controller.*;
import com.nexusengine.core.portal.service.PmsProductSemanticSearchService;
import com.nexusengine.core.portal.service.impl.*;
import com.nexusengine.core.portal.controller.*;
import com.nexusengine.core.repository.PmsProductEmbeddingRepository;
import com.nexusengine.core.portal.service.impl.*;
import com.nexusengine.core.portal.controller.*;
import com.nexusengine.core.repository.PmsProductRepository;
import com.nexusengine.core.portal.service.impl.*;
import com.nexusengine.core.portal.controller.*;
import com.nexusengine.core.portal.service.impl.*;
import com.nexusengine.core.portal.controller.*;
import org.junit.jupiter.api.BeforeEach;
import com.nexusengine.core.portal.service.impl.*;
import com.nexusengine.core.portal.controller.*;
import org.junit.jupiter.api.Test;
import com.nexusengine.core.portal.service.impl.*;
import com.nexusengine.core.portal.controller.*;
import org.springframework.beans.factory.annotation.Autowired;
import com.nexusengine.core.portal.service.impl.*;
import com.nexusengine.core.portal.controller.*;
import org.springframework.boot.test.context.SpringBootTest;
import com.nexusengine.core.portal.service.impl.*;
import com.nexusengine.core.portal.controller.*;
import org.springframework.test.context.DynamicPropertyRegistry;
import com.nexusengine.core.portal.service.impl.*;
import com.nexusengine.core.portal.controller.*;
import org.springframework.test.context.DynamicPropertySource;
import com.nexusengine.core.portal.service.impl.*;
import com.nexusengine.core.portal.controller.*;
import org.testcontainers.containers.PostgreSQLContainer;
import com.nexusengine.core.portal.service.impl.*;
import com.nexusengine.core.portal.controller.*;
import org.testcontainers.junit.jupiter.Container;
import com.nexusengine.core.portal.service.impl.*;
import com.nexusengine.core.portal.controller.*;
import org.testcontainers.junit.jupiter.Testcontainers;
import com.nexusengine.core.portal.service.impl.*;
import com.nexusengine.core.portal.controller.*;
import org.testcontainers.utility.DockerImageName;

import com.nexusengine.core.portal.service.impl.*;
import com.nexusengine.core.portal.controller.*;
import org.springframework.boot.test.mock.mockito.MockBean;
import com.nexusengine.core.portal.service.impl.*;
import com.nexusengine.core.portal.controller.*;
import org.springframework.data.redis.core.RedisTemplate;
import com.nexusengine.core.portal.service.impl.*;
import com.nexusengine.core.portal.controller.*;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import com.nexusengine.core.portal.service.impl.*;
import com.nexusengine.core.portal.controller.*;
import org.springframework.data.mongodb.core.MongoTemplate;

import com.nexusengine.core.portal.service.impl.*;
import com.nexusengine.core.portal.controller.*;
import java.util.List;

import com.nexusengine.core.portal.service.impl.*;
import com.nexusengine.core.portal.controller.*;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(classes = PmsProductSemanticSearchServiceIntegrationTest.TestApplication.class)
@Testcontainers
public class PmsProductSemanticSearchServiceIntegrationTest {

    @org.springframework.boot.autoconfigure.SpringBootApplication(
        scanBasePackages = "com.nexusengine.core",
        exclude = {
            org.springframework.boot.autoconfigure.mongo.MongoAutoConfiguration.class,
            org.springframework.boot.autoconfigure.data.mongo.MongoDataAutoConfiguration.class,
            org.springframework.boot.autoconfigure.data.redis.RedisAutoConfiguration.class,
            org.springframework.boot.autoconfigure.data.redis.RedisRepositoriesAutoConfiguration.class,
            org.springframework.boot.autoconfigure.amqp.RabbitAutoConfiguration.class
        }
    )
    @org.springframework.context.annotation.ComponentScan(
        basePackages = "com.nexusengine.core",
        excludeFilters = {
            @org.springframework.context.annotation.ComponentScan.Filter(
                type = org.springframework.context.annotation.FilterType.REGEX,
                pattern = ".*RedisConfig.*"
            ),
            @org.springframework.context.annotation.ComponentScan.Filter(
                type = org.springframework.context.annotation.FilterType.REGEX,
                pattern = ".*RabbitMqConfig.*"
            )
        }
    )
    @org.springframework.data.jpa.repository.config.EnableJpaRepositories(basePackages = {"com.nexusengine.core.repository"})
    @org.springframework.boot.autoconfigure.domain.EntityScan(basePackages = {"com.nexusengine.core.model", "com.nexusengine.core.portal.domain"})
    static class TestApplication {}
    
    @MockBean
    private RabbitTemplate rabbitTemplate;
    @MockBean
    private MongoTemplate mongoTemplate;
    @MockBean
    private org.springframework.data.mongodb.core.convert.MongoConverter mongoConverter;
    @MockBean
    private org.springframework.data.mongodb.MongoDatabaseFactory mongoDatabaseFactory;
    @MockBean
    private org.springframework.data.mongodb.gridfs.GridFsTemplate gridFsTemplate;
    @MockBean
    private com.nexusengine.core.common.service.RedisService redisService;
    @MockBean
    private com.nexusengine.core.portal.repository.MemberBrandAttentionRepository memberBrandAttentionRepository;
    @MockBean
    private com.nexusengine.core.portal.repository.MemberReadHistoryRepository memberReadHistoryRepository;
    @MockBean
    private com.nexusengine.core.portal.repository.MemberProductCollectionRepository memberProductCollectionRepository;
    
    @Container
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>(DockerImageName.parse("pgvector/pgvector:pg17").asCompatibleSubstituteFor("postgres"))
            .withDatabaseName("nexuscore")
            .withUsername("postgres")
            .withPassword("postgres");

    @DynamicPropertySource
    static void configureProperties(DynamicPropertyRegistry registry) throws Exception {
        registry.add("spring.datasource.url", postgres::getJdbcUrl);
        registry.add("spring.datasource.username", postgres::getUsername);
        registry.add("spring.datasource.password", postgres::getPassword);
        registry.add("spring.jpa.hibernate.ddl-auto", () -> "create-drop");
        registry.add("spring.ai.openai.api-key", () -> "sk-fake-test-key");
        registry.add("razorpay.keyId", () -> "rzp_test_123");
        registry.add("razorpay.keySecret", () -> "rzp_test_secret_123");
        registry.add("spring.autoconfigure.exclude", () -> "org.springframework.boot.actuate.autoconfigure.data.redis.RedisReactiveHealthContributorAutoConfiguration,org.springframework.boot.actuate.autoconfigure.data.redis.RedisHealthContributorAutoConfiguration,org.springframework.boot.actuate.autoconfigure.amqp.RabbitHealthContributorAutoConfiguration,org.springframework.boot.actuate.autoconfigure.data.mongo.MongoHealthContributorAutoConfiguration,org.springframework.boot.actuate.autoconfigure.data.mongo.MongoReactiveHealthContributorAutoConfiguration");
        
        // Ensure vector extension is created before Hibernate boots
        try (java.sql.Connection conn = java.sql.DriverManager.getConnection(postgres.getJdbcUrl(), postgres.getUsername(), postgres.getPassword());
             java.sql.Statement stmt = conn.createStatement()) {
            stmt.execute("CREATE EXTENSION IF NOT EXISTS vector;");
        }
    }

    @Autowired
    private PmsProductRepository productRepository;

    @Autowired
    private PmsProductEmbeddingRepository embeddingRepository;

    @Autowired
    private PmsProductSemanticSearchService semanticSearchService;

    @BeforeEach
    void setUp() {
        embeddingRepository.deleteAll();
        productRepository.deleteAll();
    }

    @Test
    void testPgvectorIntegration() {
        // Create a product
        PmsProduct product = new PmsProduct();
        product.setName("Test Product");
        product = productRepository.save(product);

        // Manually insert an embedding for testing
        PmsProductEmbedding embedding = new PmsProductEmbedding();
        embedding.setProductId(product.getId());
        embedding.setEmbedding(new float[]{0.1f, 0.2f, 0.3f});
        embeddingRepository.save(embedding);

        // Verify the embedding was saved correctly in PostgreSQL with pgvector extension
        List<PmsProductEmbedding> embeddings = embeddingRepository.findAll();
        assertEquals(1, embeddings.size());
        assertEquals(0.1f, embeddings.get(0).getEmbedding()[0], 0.001);
    }
}
