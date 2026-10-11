package com.nexusengine.core.portal.catalog.service;

import com.nexusengine.core.domain.catalog.entity.Product;


import java.util.List;


/**
 * Product Recommendation Service
 */
public interface SemanticSearchService {

    /**
     * Generate and store vector embeddings for all products in the database
     */
    int generateAllProductEmbeddings();

    /**
     * Recommend products based on a given user's browsing/search context
     */
    List<Product> semanticSearch(Long memberId, String searchContext);
}
