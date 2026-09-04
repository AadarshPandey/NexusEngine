package com.nexusengine.core.portal.domain;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;
import java.util.List;

/**
 * Auto-generated documentation
 * Created by macro on 2019/1/28.
 */
@Getter
@Setter
public class HomeFlashPromotion {
    @Schema(title = "Start time")
    private Date startTime;
    @Schema(title = "End time")
    private Date endTime;
    @Schema(title = "Next start time")
    private Date nextStartTime;
    @Schema(title = "Next end time")
    private Date nextEndTime;
    @Schema(title = "Product list")
    private List<FlashPromotionProduct> productList;
}
