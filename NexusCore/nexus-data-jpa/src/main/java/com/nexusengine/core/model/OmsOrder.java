package com.nexusengine.core.model;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "oms_order", schema = "orders")
public class OmsOrder implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(title = "Id")
    private Long id;

    @Column(name = "member_id")
    private Long memberId;
    @com.fasterxml.jackson.annotation.JsonIgnore
    @jakarta.persistence.ManyToOne(fetch = jakarta.persistence.FetchType.LAZY)
    @jakarta.persistence.JoinColumn(name = "member_id", insertable = false, updatable = false)
    private UmsMember member;

    @Column(name = "coupon_id")
    private Long couponId;
    @com.fasterxml.jackson.annotation.JsonIgnore
    @jakarta.persistence.ManyToOne(fetch = jakarta.persistence.FetchType.LAZY)
    @jakarta.persistence.JoinColumn(name = "coupon_id", insertable = false, updatable = false)
    private SmsCoupon coupon;

    @Column(name = "order_sn")
    @Schema(title = "Order sn")
    private String orderSn;

    @Column(name = "create_time")
    @Schema(title = "Create time")
    private LocalDateTime createTime;

    @Column(name = "total_amount")
    @Schema(title = "Total amount")
    private BigDecimal totalAmount;

    @Column(name = "pay_amount")
    @Schema(title = "Pay amount")
    private BigDecimal payAmount;

    @Column(name = "freight_amount")
    @Schema(title = "Freight amount")
    private BigDecimal freightAmount;

    @Column(name = "promotion_amount")
    @Schema(title = "Promotion amount")
    private BigDecimal promotionAmount;

    @Column(name = "coupon_amount")
    @Schema(title = "Coupon amount")
    private BigDecimal couponAmount;

    @Column(name = "discount_amount")
    @Schema(title = "Discount amount")
    private BigDecimal discountAmount;

    @Column(name = "pay_type")
    @Schema(title = "Pay type")
    private Integer payType;

    @Schema(title = "Status")
    private Integer status;

    @Column(name = "order_type")
    @Schema(title = "Order type")
    private Integer orderType;

    @Column(name = "delivery_company")
    @Schema(title = "Delivery company")
    private String deliveryCompany;

    @Column(name = "delivery_sn")
    @Schema(title = "Delivery sn")
    private String deliverySn;

    @Column(name = "receiver_name")
    @Schema(title = "Receiver name")
    private String receiverName;

    @Column(name = "receiver_phone")
    @Schema(title = "Receiver phone")
    private String receiverPhone;

    @Column(name = "receiver_post_code")
    @Schema(title = "Receiver post code")
    private String receiverPostCode;

    @Column(name = "receiver_detail_address")
    @Schema(title = "Receiver detail address")
    private String receiverDetailAddress;

    @Schema(title = "Note")
    private String note;

    @Column(name = "confirm_status")
    @Schema(title = "Confirm status")
    private Integer confirmStatus;

    @Column(name = "delete_status")
    @Schema(title = "Delete status")
    private Integer deleteStatus;

    @Column(name = "payment_time")
    @Schema(title = "Payment time")
    private LocalDateTime paymentTime;

    @Column(name = "delivery_time")
    @Schema(title = "Delivery time")
    private LocalDateTime deliveryTime;

    @Column(name = "receive_time")
    @Schema(title = "Receive time")
    private LocalDateTime receiveTime;

    @Column(name = "comment_time")
    @Schema(title = "Comment time")
    private LocalDateTime commentTime;

    @Column(name = "modify_time")
    @Schema(title = "Modify time")
    private LocalDateTime modifyTime;

}
