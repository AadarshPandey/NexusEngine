package com.nexusengine.core.dto;

import com.nexusengine.core.model.SmsFlashPromotionSession;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

/**
 * Represents the SmsFlashPromotionSessionDetail component.
 * Provides core functionality and operations for SmsFlashPromotionSessionDetail.
 */
public class SmsFlashPromotionSessionDetail extends SmsFlashPromotionSession {
    @Setter
    @Getter
    @Schema(title = "Product count")
    private Long productCount;
}
