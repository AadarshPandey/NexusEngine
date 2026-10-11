package com.nexusengine.core.portal.ordering.dto;

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
