package com.nexusengine.core.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.time.OffsetDateTime;
import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name = "flash_sale_purchase", schema = "promotion")
public class SmsFlashPromotionLog implements Serializable {
        @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    
private Long id;

        @Column(name = "member_id", nullable = false)
    
private Long memberId;
        @com.fasterxml.jackson.annotation.JsonIgnore
    @jakarta.persistence.ManyToOne(fetch = jakarta.persistence.FetchType.LAZY)
    @jakarta.persistence.JoinColumn(name = "member_id", insertable = false, updatable = false)
    
private UmsMember member;

        @Column(name = "product_id", nullable = false)
    
private Long productId;
        @com.fasterxml.jackson.annotation.JsonIgnore
    @jakarta.persistence.ManyToOne(fetch = jakarta.persistence.FetchType.LAZY)
    @jakarta.persistence.JoinColumn(name = "product_id", insertable = false, updatable = false)
    
private PmsProduct product;

        @Column(name = "flash_sale_id")
    
private Long flashPromotionId;
        @com.fasterxml.jackson.annotation.JsonIgnore
    @jakarta.persistence.ManyToOne(fetch = jakarta.persistence.FetchType.LAZY)
    @jakarta.persistence.JoinColumn(name = "flash_sale_id", insertable = false, updatable = false)
    
private SmsFlashPromotion flashPromotion;

        @Column(name = "slot_id")
    
private Long sessionId;
        @com.fasterxml.jackson.annotation.JsonIgnore
    @jakarta.persistence.ManyToOne(fetch = jakarta.persistence.FetchType.LAZY)
    @jakarta.persistence.JoinColumn(name = "slot_id", insertable = false, updatable = false)
    
private SmsFlashPromotionSession session;

        @Column(name = "order_id")
    
private Long orderId;
        @com.fasterxml.jackson.annotation.JsonIgnore
    @jakarta.persistence.ManyToOne(fetch = jakarta.persistence.FetchType.LAZY)
    @jakarta.persistence.JoinColumn(name = "order_id", insertable = false, updatable = false)
    
private OmsOrder order;

        @Column(name = "quantity")
    
private Integer quantity;

        @Column(name = "status")
    
private String status;

        @Column(name = "created_at")
    
private OffsetDateTime createTime;
}
