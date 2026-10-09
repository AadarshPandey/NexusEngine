package com.nexusengine.core.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.math.BigDecimal;

@Getter
@Setter
@Entity
@Table(name = "flash_sale_item", schema = "promotion")
public class SmsFlashPromotionProductRelation implements Serializable {
        @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    
private Long id;

        @Column(name = "flash_sale_id", nullable = false)
    
private Long flashPromotionId;
        @com.fasterxml.jackson.annotation.JsonIgnore
    @jakarta.persistence.ManyToOne(fetch = jakarta.persistence.FetchType.LAZY)
    @jakarta.persistence.JoinColumn(name = "flash_sale_id", insertable = false, updatable = false)
    
private SmsFlashPromotion flashPromotion;

        @Column(name = "slot_id", nullable = false)
    
private Long flashPromotionSessionId;
        @com.fasterxml.jackson.annotation.JsonIgnore
    @jakarta.persistence.ManyToOne(fetch = jakarta.persistence.FetchType.LAZY)
    @jakarta.persistence.JoinColumn(name = "slot_id", insertable = false, updatable = false)
    
private SmsFlashPromotionSession flashPromotionSession;

        @Column(name = "product_id", nullable = false)
    
private Long productId;
        @com.fasterxml.jackson.annotation.JsonIgnore
    @jakarta.persistence.ManyToOne(fetch = jakarta.persistence.FetchType.LAZY)
    @jakarta.persistence.JoinColumn(name = "product_id", insertable = false, updatable = false)
    
private PmsProduct product;

        @Column(name = "flash_price", nullable = false)
    
private BigDecimal flashPromotionPrice;

        @Column(name = "quantity_available", nullable = false)
    
private Integer flashPromotionCount;

        @Column(name = "per_member_limit")
    @org.hibernate.annotations.JdbcTypeCode(org.hibernate.type.SqlTypes.SMALLINT)
private Integer flashPromotionLimit;
}
