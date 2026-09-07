package com.nexusengine.core.portal.domain;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
public class FlashSaleOrderMessage implements Serializable {
    private Long memberId;
    private Long productId;
    private Long flashPromotionId;
    private Long flashPromotionSessionId;
    private Integer quantity;
}
