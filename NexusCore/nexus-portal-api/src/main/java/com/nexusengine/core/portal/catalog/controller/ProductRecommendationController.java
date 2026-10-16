package com.nexusengine.core.portal.catalog.controller;

import com.nexusengine.core.portal.catalog.dto.ProductSummaryResponse;
import com.nexusengine.core.portal.catalog.mapper.PortalProductMapper;
import com.nexusengine.core.portal.catalog.service.SemanticSearchService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Tag(name = "Storefront - Recommendations", description = "AI product recommendation APIs")
@RequestMapping("/portal/recommendation")
@RequiredArgsConstructor
public class ProductRecommendationController {

    private final SemanticSearchService semanticSearchService;
    private final PortalProductMapper portalProductMapper;

    @Operation(summary = "Generate product embeddings")
    @PostMapping("/generateEmbeddings")
    public Integer generateEmbeddings() {
        return semanticSearchService.generateAllProductEmbeddings();
    }

    @Operation(summary = "Get AI recommendations based on context")
    @GetMapping("/products")
    public List<ProductSummaryResponse> semanticSearch(
            @RequestParam(required = false) Long memberId,
            @RequestParam("searchContext") String searchContext) {
        return portalProductMapper.toSummaryList(
                semanticSearchService.semanticSearch(memberId, searchContext));
    }
}
