package com.nexusengine.core.portal.domain;

import lombok.Getter;
import lombok.Setter;
import java.io.Serializable;

public record FlashSaleOrderMessage(
    Long memberId,
    Long productId,
    Long flashPromotionId,
    Long flashPromotionSessionId,
    Integer quantity
) implements Serializable {
}
