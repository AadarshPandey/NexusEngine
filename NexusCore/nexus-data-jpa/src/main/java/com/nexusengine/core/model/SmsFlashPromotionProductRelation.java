package com.nexusengine.core.model;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "sms_flash_promotion_product_relation")
public class SmsFlashPromotionProductRelation implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(title = "Id")
    private Long id;

    @Column(name = "flash_promotion_id")
    private Long flashPromotionId;

    @Column(name = "flash_promotion_session_id")
    @Schema(title = "Flash promotion session id")
    private Long flashPromotionSessionId;

    @Column(name = "product_id")
    private Long productId;

    @Column(name = "flash_promotion_price")
    @Schema(title = "Flash promotion price")
    private BigDecimal flashPromotionPrice;

    @Column(name = "flash_promotion_count")
    @Schema(title = "Flash promotion count")
    private Integer flashPromotionCount;

    @Column(name = "flash_promotion_limit")
    @Schema(title = "Flash promotion limit")
    private Integer flashPromotionLimit;

    @Schema(title = "Sort")
    private Integer sort;
}
