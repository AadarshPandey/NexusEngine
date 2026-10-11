package com.nexusengine.core.portal.promotion.dto;



import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDateTime;
import java.util.List;


/**
 * Represents the HomeFlashPromotion component.
 * Provides core functionality and operations for HomeFlashPromotion.
 */
public record HomeFlashPromotion(
    @Schema(title = "Start time") LocalDateTime startTime,
    @Schema(title = "End time") LocalDateTime endTime,
    @Schema(title = "Next start time") LocalDateTime nextStartTime,
    @Schema(title = "Next end time") LocalDateTime nextEndTime,
    @Schema(title = "Product list") List<FlashPromotionProduct> productList
) {
}
