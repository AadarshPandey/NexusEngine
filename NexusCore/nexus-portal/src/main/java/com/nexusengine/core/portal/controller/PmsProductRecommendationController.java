package com.nexusengine.core.portal.controller;

import org.springframework.web.bind.annotation.RestController;
import com.nexusengine.core.common.api.CommonResult;
import com.nexusengine.core.model.PmsProduct;
import com.nexusengine.core.portal.service.PmsProductSemanticSearchService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Tag(name = "PmsProductRecommendationController", description = "AI Product Recommendation APIs")
@RequestMapping("/portal/recommendation")
@lombok.RequiredArgsConstructor
public class PmsProductRecommendationController {

    private final PmsProductSemanticSearchService semanticSearchService;

    @Operation(summary = "Generate product embeddings (Admin/Cron trigger)")
    @PostMapping("/generateEmbeddings")

    public CommonResult<Integer> generateEmbeddings() {
        int count = semanticSearchService.generateAllProductEmbeddings();
        return CommonResult.success(count, "Successfully generated embeddings for " + count + " products.");
    }

    @Operation(summary = "Get AI recommendations based on context")
    @GetMapping("/products")

    public CommonResult<List<PmsProduct>> semanticSearch(
            @RequestParam(required = false) Long memberId,
            @RequestParam String searchContext) {
        List<PmsProduct> productList = semanticSearchService.semanticSearch(memberId, searchContext);
        return CommonResult.success(productList);
    }
}
