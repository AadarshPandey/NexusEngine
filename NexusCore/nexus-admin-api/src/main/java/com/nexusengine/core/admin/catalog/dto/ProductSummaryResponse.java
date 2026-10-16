package com.nexusengine.core.admin.catalog.dto;

import java.math.BigDecimal;

public record ProductSummaryResponse(
        Long id,
        Long brandId,
        Long productCategoryId,
        String name,
        String productCode,
        String primaryImageUrl,
        Boolean isPublished,
        Boolean isNewArrival,
        Boolean isFeatured,
        Integer approvalStatus,
        Integer unitsSold,
        BigDecimal listPrice,
        String brandName) {}
