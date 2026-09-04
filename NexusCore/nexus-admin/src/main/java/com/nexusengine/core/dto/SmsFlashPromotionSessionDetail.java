package com.nexusengine.core.dto;

import com.nexusengine.core.model.SmsFlashPromotionSession;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

/**
 * Auto-generated documentation
 * Created by macro on 2018/11/19.
 */
public class SmsFlashPromotionSessionDetail extends SmsFlashPromotionSession {
    @Setter
    @Getter
    @Schema(title = "Product count")
    private Long productCount;
}
