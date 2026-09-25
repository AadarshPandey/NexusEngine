package com.nexusengine.core.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.math.BigDecimal;

@Getter
@Setter
@Entity
@Table(name = "sms_flash_promotion_product_relation")
public class SmsFlashPromotionProductRelation implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "flash_promotion_id", nullable = false)
    private Long flashPromotionId;

    @Column(name = "flash_promotion_session_id", nullable = false)
    private Long flashPromotionSessionId;

    @Column(name = "product_id", nullable = false)
    private Long productId;

    @Column(name = "flash_promotion_price", nullable = false)
    private BigDecimal flashPromotionPrice;

    @Column(name = "flash_promotion_count", nullable = false)
    private Integer flashPromotionCount;

    @Column(name = "flash_promotion_limit")
    private Integer flashPromotionLimit;

    @Column(name = "sort")
    private Integer sort;
}
