package com.nexusengine.core.portal.catalog.controller;

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

import org.springframework.web.bind.annotation.RestController;

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
import com.nexusengine.core.common.api.CommonResult;
import com.nexusengine.core.domain.catalog.entity.Product;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;


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
