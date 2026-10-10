package com.nexusengine.core.domain.promotion.entity;

import com.nexusengine.core.domain.ordering.entity.CustomerOrder;
import com.nexusengine.core.domain.customer.entity.Member;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import java.io.Serializable;
import java.time.OffsetDateTime;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "member_coupon", schema = "promotion")
public class MemberCoupon implements Serializable {

    private static final long serialVersionUID = 1L;

        @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    
private Long id;

        @Column(name = "coupon_id")
    
private Long couponId;
        @com.fasterxml.jackson.annotation.JsonIgnore
    @jakarta.persistence.ManyToOne(fetch = jakarta.persistence.FetchType.LAZY)
    @jakarta.persistence.JoinColumn(name = "coupon_id", insertable = false, updatable = false)
    
private Coupon coupon;

        @Column(name = "member_id")
    
private Long memberId;
        @com.fasterxml.jackson.annotation.JsonIgnore
    @jakarta.persistence.ManyToOne(fetch = jakarta.persistence.FetchType.LAZY)
    @jakarta.persistence.JoinColumn(name = "member_id", insertable = false, updatable = false)
    
private Member member;

            @org.hibernate.annotations.Formula("(SELECT c.code FROM promotion.coupon c WHERE c.id = coupon_id)")
    private String couponCode;

            @org.hibernate.annotations.Formula("(SELECT m.display_name FROM customer.member m WHERE m.id = member_id)")
    private String memberNickname;

        @Column(name = "acquisition_channel")
    @Schema(title = "Get type")
    @org.hibernate.annotations.JdbcTypeCode(org.hibernate.type.SqlTypes.SMALLINT)
private Integer getType;

        @Column(name = "claimed_at")
    
private OffsetDateTime createTime;

        @Column(name = "status")
    @Schema(title = "Use status")
    @org.hibernate.annotations.JdbcTypeCode(org.hibernate.type.SqlTypes.SMALLINT)
private Integer useStatus;

        @Column(name = "redeemed_at")
    @Schema(title = "Use time")
    
private OffsetDateTime useTime;

        @Column(name = "order_id")
    @Schema(title = "Order id")
    
private Long orderId;
        @com.fasterxml.jackson.annotation.JsonIgnore
    @jakarta.persistence.ManyToOne(fetch = jakarta.persistence.FetchType.LAZY)
    @jakarta.persistence.JoinColumn(name = "order_id", insertable = false, updatable = false)
    
private CustomerOrder order;

            @org.hibernate.annotations.Formula("(SELECT o.order_number FROM ordering.customer_order o WHERE o.id = order_id)")
    private String orderSn;
}
