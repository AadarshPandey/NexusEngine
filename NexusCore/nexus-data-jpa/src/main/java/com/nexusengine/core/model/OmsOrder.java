package com.nexusengine.core.model;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import java.io.Serializable;
import java.time.OffsetDateTime;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "customer_order", schema = "ordering")
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

        @Column(name = "order_number")
    @Schema(title = "Order sn")
    
private String orderSn;

        @Column(name = "created_at")
    @Schema(title = "Create time")
    
private OffsetDateTime createTime;

        @Column(name = "subtotal_amount")
    @Schema(title = "Total amount")
    
private BigDecimal totalAmount;

        @Column(name = "grand_total_amount")
    @Schema(title = "Pay amount")
    
private BigDecimal payAmount;

        @Column(name = "shipping_amount")
    @Schema(title = "Freight amount")
    
private BigDecimal freightAmount;

        @Column(name = "promotion_discount_amount")
    @Schema(title = "Promotion amount")
    
private BigDecimal promotionAmount;

        @Column(name = "coupon_discount_amount")
    @Schema(title = "Coupon amount")
    
private BigDecimal couponAmount;

        @Column(name = "manual_adjustment_amount")
    @Schema(title = "Discount amount")
    
private BigDecimal discountAmount;

        @Column(name = "payment_method")
    @Schema(title = "Pay type")
    @org.hibernate.annotations.JdbcTypeCode(org.hibernate.type.SqlTypes.SMALLINT)
private Integer payType;

        @Schema(title = "Status")
    @org.hibernate.annotations.JdbcTypeCode(org.hibernate.type.SqlTypes.SMALLINT)
private Integer status;

        @Column(name = "order_type")
    @Schema(title = "Order type")
    @org.hibernate.annotations.JdbcTypeCode(org.hibernate.type.SqlTypes.SMALLINT)
private Integer orderType;

        @Column(name = "carrier_name")
    @Schema(title = "Delivery company")
    
private String deliveryCompany;

        @Column(name = "tracking_number")
    @Schema(title = "Delivery sn")
    
private String deliverySn;

        @Column(name = "recipient_name")
    @Schema(title = "Receiver name")
    
private String receiverName;

        @Column(name = "recipient_phone")
    @Schema(title = "Receiver phone")
    
private String receiverPhone;

        @Column(name = "shipping_postal_code")
    @Schema(title = "Receiver post code")
    
private String receiverPostCode;

        @Column(name = "shipping_address_line")
    @Schema(title = "Receiver detail address")
    
private String receiverDetailAddress;

            @Schema(title = "Note")
    @Column(name = "customer_note")
private String note;

        @Column(name = "is_receipt_confirmed")
    @Schema(title = "Confirm status")
    @jakarta.persistence.Convert(converter = com.nexusengine.core.model.converter.IntegerToBooleanConverter.class)
private Integer confirmStatus;

        @Convert(converter = com.nexusengine.core.model.converter.IntegerToDeletedAtConverter.class)
    @Column(name = "deleted_at")
    @Schema(title = "Delete status")
    
private Integer deleteStatus;

        @Column(name = "paid_at")
    @Schema(title = "Payment time")
    
private OffsetDateTime paymentTime;

        @Column(name = "shipped_at")
    @Schema(title = "Delivery time")
    
private OffsetDateTime deliveryTime;

        @Column(name = "delivered_at")
    @Schema(title = "Receive time")
    
private OffsetDateTime receiveTime;

        @Column(name = "reviewed_at")
    @Schema(title = "Comment time")
    
private OffsetDateTime commentTime;

        @Column(name = "updated_at")
    @Schema(title = "Modify time")
    
private OffsetDateTime modifyTime;


    @jakarta.persistence.Version
    private Integer version;
}
