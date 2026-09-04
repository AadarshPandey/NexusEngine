package com.nexusengine.core.portal.service;

import com.nexusengine.core.model.PmsProduct;

import java.util.List;

/**
 * Product Recommendation Service
 */
public interface PmsProductSemanticSearchService {

    /**
     * Generate and store vector embeddings for all products in the database
     */
    int generateAllProductEmbeddings();

    /**
     * Recommend products based on a given user's browsing/search context
     */
    List<PmsProduct> semanticSearch(Long memberId, String searchContext);
}
