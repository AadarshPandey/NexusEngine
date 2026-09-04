package com.nexusengine.core.portal.service.impl;

import com.nexusengine.core.model.PmsProduct;
import com.nexusengine.core.model.PmsProductEmbedding;
import com.nexusengine.core.portal.service.PmsProductSemanticSearchService;
import com.nexusengine.core.repository.PmsProductEmbeddingRepository;
import com.nexusengine.core.repository.PmsProductRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.embedding.EmbeddingModel;
import org.springframework.ai.embedding.EmbeddingResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class PmsProductSemanticSearchServiceImpl implements PmsProductSemanticSearchService {

    @Autowired
    private PmsProductRepository productRepository;

    @Autowired
    private PmsProductEmbeddingRepository productEmbeddingRepository;

    @Autowired(required = false)
    private EmbeddingModel embeddingModel;

    @Override
    public int generateAllProductEmbeddings() {
        if (embeddingModel == null) {
            log.error("EmbeddingModel is not available. Please check OpenAI API key.");
            return 0;
        }

        List<PmsProduct> products = productRepository.findAll();
        int count = 0;
        
        // Batch processing to avoid rate limits
        int batchSize = 100;
        for (int i = 0; i < products.size(); i += batchSize) {
            int end = Math.min(products.size(), i + batchSize);
            List<PmsProduct> batch = products.subList(i, end);
            
            List<String> contents = batch.stream()
                .map(this::buildProductContent)
                .collect(Collectors.toList());
                
            List<float[]> vectors = contents.stream()
                .map(content -> embeddingModel.embed(content))
                .collect(Collectors.toList());
                
            List<PmsProductEmbedding> embeddingsToSave = new ArrayList<>();
            for (int j = 0; j < batch.size(); j++) {
                float[] vector = vectors.get(j);
                if (vector != null && vector.length > 0) {
                    PmsProductEmbedding embedding = new PmsProductEmbedding();
                    embedding.setProductId(batch.get(j).getId());
                    embedding.setEmbedding(vector);
                    embeddingsToSave.add(embedding);
                    count++;
                }
            }
            productEmbeddingRepository.saveAll(embeddingsToSave);
            
            // Sleep to avoid rate limits
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
        return count;
    }

    @Override
    @io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker(name = "openai", fallbackMethod = "semanticSearchFallback")
    @io.github.resilience4j.retry.annotation.Retry(name = "openai")
    public List<PmsProduct> semanticSearch(Long memberId, String searchContext) {
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

    public List<PmsProduct> semanticSearchFallback(Long memberId, String searchContext, Throwable t) {
        log.warn("OpenAI/pgvector service temporarily unavailable, falling back to static/cached recommendations: {}", t.getMessage());
        // Simple fallback: just return an empty list or the top latest products.
        // Returning an empty list gracefully degrades the UI.
        return new ArrayList<>();
    }

    private String buildProductContent(PmsProduct product) {
        return String.format("%s %s %s %s",
                product.getName() != null ? product.getName() : "",
                product.getSubTitle() != null ? product.getSubTitle() : "",
                product.getDescription() != null ? product.getDescription() : "",
                product.getKeywords() != null ? product.getKeywords() : "");
    }
}
