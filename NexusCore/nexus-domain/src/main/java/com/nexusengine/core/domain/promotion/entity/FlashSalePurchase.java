package com.nexusengine.core.domain.promotion.entity;

import com.nexusengine.core.domain.catalog.entity.Product;
import com.nexusengine.core.domain.ordering.entity.CustomerOrder;
import com.nexusengine.core.domain.customer.entity.Member;

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
public class FlashSalePurchase implements Serializable {
        @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    
private Long id;

        @Column(name = "member_id", nullable = false)
    
private Long memberId;
        @com.fasterxml.jackson.annotation.JsonIgnore
    @jakarta.persistence.ManyToOne(fetch = jakarta.persistence.FetchType.LAZY)
    @jakarta.persistence.JoinColumn(name = "member_id", insertable = false, updatable = false)
    
private Member member;

        @Column(name = "product_id", nullable = false)
    
private Long productId;
        @com.fasterxml.jackson.annotation.JsonIgnore
    @jakarta.persistence.ManyToOne(fetch = jakarta.persistence.FetchType.LAZY)
    @jakarta.persistence.JoinColumn(name = "product_id", insertable = false, updatable = false)
    
private Product product;

        @Column(name = "flash_sale_id")
    
private Long flashPromotionId;
        @com.fasterxml.jackson.annotation.JsonIgnore
    @jakarta.persistence.ManyToOne(fetch = jakarta.persistence.FetchType.LAZY)
    @jakarta.persistence.JoinColumn(name = "flash_sale_id", insertable = false, updatable = false)
    
private FlashSale flashPromotion;

        @Column(name = "slot_id")
    
private Long sessionId;
        @com.fasterxml.jackson.annotation.JsonIgnore
    @jakarta.persistence.ManyToOne(fetch = jakarta.persistence.FetchType.LAZY)
    @jakarta.persistence.JoinColumn(name = "slot_id", insertable = false, updatable = false)
    
private FlashSaleSlot session;

        @Column(name = "order_id")
    
private Long orderId;
        @com.fasterxml.jackson.annotation.JsonIgnore
    @jakarta.persistence.ManyToOne(fetch = jakarta.persistence.FetchType.LAZY)
    @jakarta.persistence.JoinColumn(name = "order_id", insertable = false, updatable = false)
    
private CustomerOrder order;

        @Column(name = "quantity")
    
private Integer quantity;

        @Column(name = "status")
    
private String status;

        @Column(name = "created_at")
    
private OffsetDateTime createTime;
}
