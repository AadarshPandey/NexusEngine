package com.nexusengine.core.portal.domain;

import com.nexusengine.core.model.PmsProduct;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

/**
 * Auto-generated documentation
 * Created by macro on 2019/1/28.
 */
@Getter
@Setter
public class FlashPromotionProduct extends PmsProduct{
    @Schema(title = "Flash promotion price")
    private BigDecimal flashPromotionPrice;
    @Schema(title = "Flash promotion count")
    private Integer flashPromotionCount;
    @Schema(title = "Flash promotion limit")
    private Integer flashPromotionLimit;
}
