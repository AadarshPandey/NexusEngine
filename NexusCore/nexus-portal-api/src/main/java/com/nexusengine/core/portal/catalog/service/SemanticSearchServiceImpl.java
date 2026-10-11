package com.nexusengine.core.portal.catalog.service;

import com.nexusengine.core.domain.catalog.entity.Product;
import com.nexusengine.core.domain.catalog.repository.ProductRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.document.Document;
import org.springframework.ai.vectorstore.SearchRequest;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

@Slf4j
@Service
public class SemanticSearchServiceImpl implements SemanticSearchService {

    private final ProductRepository productRepository;
    private final VectorStore vectorStore;

    public SemanticSearchServiceImpl(
            ProductRepository productRepository,
            @Autowired(required = false) VectorStore vectorStore) {
        this.productRepository = productRepository;
        this.vectorStore = vectorStore;
    }

    @Override
    public int generateAllProductEmbeddings() {
        if (vectorStore == null) {
            log.error("VectorStore is not available.");
            return 0;
        }

        List<Product> products = productRepository.findAll(PageRequest.of(0, 1000)).getContent();
        if (products.isEmpty()) {
            return 0;
        }

        List<Document> docs = products.stream()
                .map(p -> new Document(buildProductContent(p), Map.of("productId", p.getId())))
                .collect(Collectors.toList());

        vectorStore.add(docs);
        return docs.size();
    }

    @Override
    @org.springframework.transaction.annotation.Transactional(propagation = org.springframework.transaction.annotation.Propagation.NOT_SUPPORTED)
    @io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker(name = "gemini", fallbackMethod = "semanticSearchFallback")
    @io.github.resilience4j.retry.annotation.Retry(name = "gemini")
    public List<Product> semanticSearch(Long memberId, String searchContext) {
        if (vectorStore == null || searchContext == null || searchContext.trim().isEmpty()) {
            return new ArrayList<>();
        }

        List<Document> results = vectorStore.similaritySearch(
                SearchRequest.builder().query(searchContext).topK(5).build()
        );

        if (results == null || results.isEmpty()) {
            return new ArrayList<>();
        }

        List<Long> productIds = results.stream()
                .map(doc -> {
                    Object idObj = doc.getMetadata().get("productId");
                    if (idObj instanceof Number num) {
                        return num.longValue();
                    } else if (idObj != null) {
                        try {
                            return Long.parseLong(idObj.toString());
                        } catch (NumberFormatException ignored) {
                        }
                    }
                    return null;
                })
                .filter(Objects::nonNull)
                .distinct()
                .collect(Collectors.toList());

        if (productIds.isEmpty()) {
            return new ArrayList<>();
        }

        return productRepository.findAllById(productIds);
    }

    public List<Product> semanticSearchFallback(Long memberId, String searchContext, Throwable t) {
        log.warn("Vector search service temporarily unavailable, falling back to empty list: {}", t.getMessage());
        return new ArrayList<>();
    }

    private String buildProductContent(Product product) {
        return String.format("%s %s %s",
                product.getName() != null ? product.getName() : "",
                product.getDescription() != null ? product.getDescription() : "",
                product.getKeywords() != null ? product.getKeywords() : "");
    }
}
