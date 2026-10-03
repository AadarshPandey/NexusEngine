package com.nexusengine.core.portal.domain;

import com.nexusengine.core.model.PmsBrand;
import com.nexusengine.core.model.PmsProduct;
import com.nexusengine.core.model.PromotionalBanner;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
import java.util.List;

/**
 * Represents the HomeContentResult component.
 * Provides core functionality and operations for HomeContentResult.
 */
public record HomeContentResult(
    @Schema(title = "Advertise list") List<PromotionalBanner> advertiseList,
    @Schema(title = "Brand list") List<PmsBrand> brandList,
    @Schema(title = "Home flash promotion") HomeFlashPromotion homeFlashPromotion,
    @Schema(title = "New product list") List<PmsProduct> newProductList,
    @Schema(title = "Hot product list") List<PmsProduct> hotProductList,
    @Schema(title = "AI Recommended product list") List<PmsProduct> aiRecommendProductList
) {
}
