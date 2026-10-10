package com.nexusengine.core.search.repository;

import com.nexusengine.core.search.domain.ProductDocument;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Represents the ProductSearchRepository component.
 * Provides core functionality and operations for ProductSearchRepository.
 */
public interface ProductSearchRepository extends ElasticsearchRepository<ProductDocument, Long> {
    /**
     *
     */
    Page<ProductDocument> findByNameOrKeywords(String name, String keywords, Pageable page);

}
