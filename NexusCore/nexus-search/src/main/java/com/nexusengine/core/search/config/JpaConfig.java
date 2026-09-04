package com.nexusengine.core.search.config;

import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * JPA configuration for search module
 */
@Configuration
@EnableTransactionManagement
@EnableJpaRepositories(basePackages = {"com.nexusengine.core.repository"})
@EntityScan(basePackages = {"com.nexusengine.core.model"})
public class JpaConfig {
}
