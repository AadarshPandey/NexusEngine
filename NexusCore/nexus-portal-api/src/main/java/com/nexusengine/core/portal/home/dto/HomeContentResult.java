package com.nexusengine.core.portal.home.dto;

import com.nexusengine.core.domain.catalog.entity.Brand;

import com.nexusengine.core.domain.catalog.entity.Product;
import com.nexusengine.core.domain.promotion.entity.Banner;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
import java.util.List;
import com.nexusengine.core.portal.promotion.dto.HomeFlashPromotion;


/**
 * Represents the HomeContentResult component.
 * Provides core functionality and operations for HomeContentResult.
 */
public record HomeContentResult(
    @Schema(title = "Advertise list") List<Banner> advertiseList,
    @Schema(title = "Brand list") List<Brand> brandList,
    @Schema(title = "Home flash promotion") HomeFlashPromotion homeFlashPromotion,
    @Schema(title = "New product list") List<Product> newProductList,
    @Schema(title = "Hot product list") List<Product> hotProductList,
    @Schema(title = "AI Recommended product list") List<Product> aiRecommendProductList
) {
}
