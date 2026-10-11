package com.nexusengine.core.portal.catalog.controller;

import lombok.RequiredArgsConstructor;

import org.springframework.web.bind.annotation.RestController;

import com.nexusengine.core.common.api.CommonResult;
import com.nexusengine.core.domain.catalog.entity.Product;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import com.nexusengine.core.portal.catalog.service.SemanticSearchService;


@RestController
@Tag(name = "ProductRecommendationController", description = "AI Product Recommendation APIs")
@RequestMapping("/portal/recommendation")
@RequiredArgsConstructor
public class ProductRecommendationController {

    private final SemanticSearchService semanticSearchService;

    @Operation(summary = "Generate product embeddings (Admin/Cron trigger)")
    @PostMapping("/generateEmbeddings")

    public CommonResult<Integer> generateEmbeddings() {
        int count = semanticSearchService.generateAllProductEmbeddings();
        return CommonResult.success(count, "Successfully generated embeddings for " + count + " products.");
    }

    @Operation(summary = "Get AI recommendations based on context")
    @GetMapping("/products")

    public CommonResult<List<Product>> semanticSearch(
            @RequestParam(required = false) Long memberId,
            @RequestParam("searchContext") String searchContext) {
        List<Product> productList = semanticSearchService.semanticSearch(memberId, searchContext);
        return CommonResult.success(productList);
    }
}
