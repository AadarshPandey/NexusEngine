package com.nexusengine.core.domain.promotion.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.Formula;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import com.nexusengine.core.domain.ordering.entity.CustomerOrder;
import com.nexusengine.core.domain.customer.entity.Member;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import java.time.OffsetDateTime;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "member_coupon", schema = "promotion")
public class MemberCoupon {
        @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)

private Long id;

        @Column(name = "coupon_id")

private Long couponId;
        @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "coupon_id", insertable = false, updatable = false)

private Coupon coupon;

        @Column(name = "member_id")

private Long memberId;
        @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", insertable = false, updatable = false)

private Member member;

            @Formula("(SELECT c.code FROM promotion.coupon c WHERE c.id = coupon_id)")
    private String couponCode;

            @Formula("(SELECT m.display_name FROM customer.member m WHERE m.id = member_id)")
    private String memberNickname;

        @Column(name = "acquisition_channel")
    @Schema(title = "Get type")
    @JdbcTypeCode(SqlTypes.SMALLINT)
private Integer getType;

        @Column(name = "claimed_at")

private OffsetDateTime createTime;

        @Column(name = "status")
    @Schema(title = "Use status")
    @JdbcTypeCode(SqlTypes.SMALLINT)
private Integer useStatus;

        @Column(name = "redeemed_at")
    @Schema(title = "Use time")

private OffsetDateTime useTime;

        @Column(name = "order_id")
    @Schema(title = "Order id")

private Long orderId;
        @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id", insertable = false, updatable = false)

private CustomerOrder order;

            @Formula("(SELECT o.order_number FROM ordering.customer_order o WHERE o.id = order_id)")
    private String orderSn;
}
