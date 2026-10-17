package com.nexusengine.core.search.component;

import com.nexusengine.core.search.repository.ProductSearchRepository;
import com.nexusengine.core.search.service.ProductSearchService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

/**
 * Automatically initializes and synchronizes the Elasticsearch product search index on application startup
 * if the index is empty.
 */
@Component
public class ProductSearchIndexInitializer implements ApplicationRunner {

    private static final Logger LOGGER = LoggerFactory.getLogger(ProductSearchIndexInitializer.class);

    private final ProductSearchRepository searchRepository;
    private final ProductSearchService searchService;

    public ProductSearchIndexInitializer(ProductSearchRepository searchRepository, ProductSearchService searchService) {
        this.searchRepository = searchRepository;
        this.searchService = searchService;
    }

    @Override
    public void run(ApplicationArguments args) {
        try {
            long count = searchRepository.count();
            if (count == 0) {
                LOGGER.info("[ProductSearchIndexInitializer] Elasticsearch product index is empty. Auto-populating catalog search index...");
                int imported = searchService.importAll();
                LOGGER.info("[ProductSearchIndexInitializer] Successfully indexed {} products into Elasticsearch", imported);
            } else {
                LOGGER.info("[ProductSearchIndexInitializer] Elasticsearch product index already populated (count: {})", count);
            }
        } catch (Exception e) {
            LOGGER.warn("[ProductSearchIndexInitializer] Elasticsearch index auto-initialization skipped or deferred: {}", e.getMessage());
        }
    }
}
