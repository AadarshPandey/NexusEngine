package com.nexusengine.core.model;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "sms_coupon_history", schema = "marketing")
public class SmsCouponHistory implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "coupon_id")
    private Long couponId;
    @com.fasterxml.jackson.annotation.JsonIgnore
    @jakarta.persistence.ManyToOne(fetch = jakarta.persistence.FetchType.LAZY)
    @jakarta.persistence.JoinColumn(name = "coupon_id", insertable = false, updatable = false)
    private SmsCoupon coupon;

    @Column(name = "member_id")
    private Long memberId;
    @com.fasterxml.jackson.annotation.JsonIgnore
    @jakarta.persistence.ManyToOne(fetch = jakarta.persistence.FetchType.LAZY)
    @jakarta.persistence.JoinColumn(name = "member_id", insertable = false, updatable = false)
    private UmsMember member;

    @Column(name = "coupon_code")
    private String couponCode;

    @Column(name = "member_nickname")
    @Schema(title = "Member nickname")
    private String memberNickname;

    @Column(name = "get_type")
    @Schema(title = "Get type")
    private Integer getType;

    @Column(name = "create_time")
    private LocalDateTime createTime;

    @Column(name = "use_status")
    @Schema(title = "Use status")
    private Integer useStatus;

    @Column(name = "use_time")
    @Schema(title = "Use time")
    private LocalDateTime useTime;

    @Column(name = "order_id")
    @Schema(title = "Order id")
    private Long orderId;
    @com.fasterxml.jackson.annotation.JsonIgnore
    @jakarta.persistence.ManyToOne(fetch = jakarta.persistence.FetchType.LAZY)
    @jakarta.persistence.JoinColumn(name = "order_id", insertable = false, updatable = false)
    private OmsOrder order;

    @Column(name = "order_sn")
    @Schema(title = "Order sn")
    private String orderSn;
}
