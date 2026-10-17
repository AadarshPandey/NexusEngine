package com.nexusengine.core.portal.home.dto;

import com.nexusengine.core.domain.catalog.entity.Brand;

import com.nexusengine.core.domain.catalog.entity.Product;
import com.nexusengine.core.domain.promotion.entity.Banner;
import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;

public record HomeContentResult(
    @Schema(title = "Advertise list") List<Banner> advertiseList,
    @Schema(title = "Brand list") List<Brand> brandList,
    @Schema(title = "New product list") List<Product> newProductList,
    @Schema(title = "Hot product list") List<Product> hotProductList,
    @Schema(title = "AI Recommended product list") List<Product> aiRecommendProductList
) {
}
