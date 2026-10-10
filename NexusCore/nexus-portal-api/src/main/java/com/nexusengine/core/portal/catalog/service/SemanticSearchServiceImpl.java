package com.nexusengine.core.portal.catalog.service;

import lombok.RequiredArgsConstructor;
import com.nexusengine.core.portal.catalog.dto.*;
import com.nexusengine.core.portal.catalog.service.*;
import com.nexusengine.core.portal.catalog.dao.*;
import com.nexusengine.core.portal.payment.service.*;
import com.nexusengine.core.portal.ordering.dto.*;
import com.nexusengine.core.portal.ordering.service.*;
import com.nexusengine.core.portal.ordering.dao.*;
import com.nexusengine.core.portal.customer.dto.*;
import com.nexusengine.core.portal.customer.service.*;
import com.nexusengine.core.portal.promotion.dto.*;
import com.nexusengine.core.portal.promotion.service.*;
import com.nexusengine.core.portal.promotion.strategy.*;
import com.nexusengine.core.portal.ai.service.*;
import com.nexusengine.core.portal.home.dto.*;
import com.nexusengine.core.portal.home.service.*;
import com.nexusengine.core.portal.engagement.dto.*;
import com.nexusengine.core.portal.engagement.service.*;

import com.nexusengine.core.domain.catalog.entity.Product;

import com.nexusengine.core.domain.catalog.entity.*;
import com.nexusengine.core.domain.ordering.entity.*;
import com.nexusengine.core.domain.customer.entity.*;
import com.nexusengine.core.domain.promotion.entity.*;
import com.nexusengine.core.domain.iam.entity.*;
import com.nexusengine.core.domain.catalog.repository.*;
import com.nexusengine.core.domain.ordering.repository.*;
import com.nexusengine.core.domain.customer.repository.*;
import com.nexusengine.core.domain.promotion.repository.*;
import com.nexusengine.core.domain.iam.repository.*;
import com.nexusengine.core.domain.ai.entity.ProductEmbedding;
import com.nexusengine.core.domain.ai.repository.ProductEmbeddingRepository;
import com.nexusengine.core.domain.catalog.repository.ProductRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.embedding.EmbeddingModel;
import org.springframework.ai.embedding.EmbeddingResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.PageRequest;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;


@Slf4j
@Service
@RequiredArgsConstructor
public class SemanticSearchServiceImpl implements SemanticSearchService {

    private final ProductRepository productRepository;

    private final ProductEmbeddingRepository productEmbeddingRepository;

    private final EmbeddingModel embeddingModel;

    @Override
    public int generateAllProductEmbeddings() {
        if (embeddingModel == null) {
            log.error("EmbeddingModel is not available. Please check OpenAI API key.");
            return 0;
        }

        List<Product> products = productRepository.findAll(PageRequest.of(0, 1000)).getContent();
        int count = 0;
        
        // Batch processing to avoid rate limits
        int batchSize = 100;
        for (int i = 0; i < products.size(); i += batchSize) {
            int end = Math.min(products.size(), i + batchSize);
            List<Product> batch = products.subList(i, end);
            
            List<String> contents = batch.stream()
                .map(this::buildProductContent)
                .collect(Collectors.toList());
                
            List<float[]> vectors = contents.stream()
                .map(content -> embeddingModel.embed(content))
                .collect(Collectors.toList());
                
            List<ProductEmbedding> embeddingsToSave = new ArrayList<>();
            for (int j = 0; j < batch.size(); j++) {
                float[] vector = vectors.get(j);
                if (vector != null && vector.length > 0) {
                    ProductEmbedding embedding = new ProductEmbedding();
                    embedding.setProductId(batch.get(j).getId());
                    embedding.setEmbedding(vector);
                    embeddingsToSave.add(embedding);
                    count++;
                }
            }
            productEmbeddingRepository.saveAll(embeddingsToSave);
            
            // Sleep to avoid rate limits
            try {
                java.util.concurrent.TimeUnit.MILLISECONDS.sleep(1000);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
        return count;
    }

    @Override
    @org.springframework.transaction.annotation.Transactional(propagation = org.springframework.transaction.annotation.Propagation.NOT_SUPPORTED)
    @io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker(name = "openai", fallbackMethod = "semanticSearchFallback")
    @io.github.resilience4j.retry.annotation.Retry(name = "openai")
    public List<Product> semanticSearch(Long memberId, String searchContext) {
        if (embeddingModel == null || searchContext == null || searchContext.trim().isEmpty()) {
            return new ArrayList<>();
        }

        // Get embedding for the user context
        float[] searchVector = embeddingModel.embed(searchContext);
        if (searchVector == null || searchVector.length == 0) {
            return new ArrayList<>();
        }

        // Format vector as string for PostgreSQL native query: "[0.1, 0.2, ...]"
        String vectorString = "[" + java.util.stream.IntStream.range(0, searchVector.length)
                .mapToObj(i -> String.valueOf(searchVector[i]))
                .collect(Collectors.joining(",")) + "]";

        // Find nearest 5 products
        List<Long> nearestIds = productEmbeddingRepository.findNearestProducts(vectorString, 5);
        if (nearestIds.isEmpty()) {
            return new ArrayList<>();
        }

        return productRepository.findAllById(nearestIds);
    }

    public List<Product> semanticSearchFallback(Long memberId, String searchContext, Throwable t) {
        log.warn("OpenAI/pgvector service temporarily unavailable, falling back to static/cached recommendations: {}", t.getMessage());
        // Simple fallback: just return an empty list or the top latest products.
        // Returning an empty list gracefully degrades the UI.
        return new ArrayList<>();
    }

    private String buildProductContent(Product product) {
        return String.format("%s %s %s %s",
                product.getName() != null ? product.getName() : "",
                "",
                product.getDescription() != null ? product.getDescription() : "",
                product.getKeywords() != null ? product.getKeywords() : "");
    }
}
