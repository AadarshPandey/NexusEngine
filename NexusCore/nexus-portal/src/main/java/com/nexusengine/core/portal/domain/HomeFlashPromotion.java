package com.nexusengine.core.portal.domain;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Represents the HomeFlashPromotion component.
 * Provides core functionality and operations for HomeFlashPromotion.
 */
@Getter
@Setter
public class HomeFlashPromotion {
    @Schema(title = "Start time")
    private LocalDateTime startTime;
    @Schema(title = "End time")
    private LocalDateTime endTime;
    @Schema(title = "Next start time")
    private LocalDateTime nextStartTime;
    @Schema(title = "Next end time")
    private LocalDateTime nextEndTime;
    @Schema(title = "Product list")
    private List<FlashPromotionProduct> productList;
}
