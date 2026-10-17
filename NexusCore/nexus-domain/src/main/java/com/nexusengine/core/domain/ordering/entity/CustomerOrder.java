package com.nexusengine.core.domain.ordering.entity;

import jakarta.persistence.FetchType;
import org.hibernate.type.SqlTypes;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.nexusengine.core.domain.customer.entity.Member;
import com.nexusengine.core.domain.promotion.entity.Coupon;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import jakarta.persistence.Version;
import java.math.BigDecimal;
import java.time.Instant;
import java.time.OffsetDateTime;
import java.util.UUID;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.annotations.SQLRestriction;
import org.hibernate.type.SqlTypes;

@Getter
@Setter
@Entity
@Table(name = "customer_order", schema = "ordering")
@SQLRestriction("deleted_at IS NULL")
public class CustomerOrder {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(title = "Id")
    private Long id;

    @Column(name = "member_id")
    private Long memberId;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", insertable = false, updatable = false)
    private Member member;

    @Column(name = "coupon_id")
    private Long couponId;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "coupon_id", insertable = false, updatable = false)
    private Coupon coupon;

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
    @JdbcTypeCode(SqlTypes.SMALLINT)
    private Integer payType;

    @Schema(title = "Status")
    @JdbcTypeCode(SqlTypes.SMALLINT)
    private Integer status;

    @Column(name = "order_type")
    @Schema(title = "Order type")
    @JdbcTypeCode(SqlTypes.SMALLINT)
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
    private Boolean confirmStatus;

    @Column(name = "deleted_at")
    @Schema(title = "Deleted at")
    private Instant deletedAt;

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

    @Version
    private Integer version;

    public String getOrderNumber() {
        return orderSn;
    }

    public OffsetDateTime getCreatedAt() {
        return createTime;
    }

    public BigDecimal getSubtotalAmount() {
        return totalAmount;
    }

    public BigDecimal getGrandTotalAmount() {
        return payAmount;
    }

    public BigDecimal getShippingAmount() {
        return freightAmount;
    }

    public BigDecimal getPromotionDiscountAmount() {
        return promotionAmount;
    }

    public BigDecimal getCouponDiscountAmount() {
        return couponAmount;
    }

    public BigDecimal getManualAdjustmentAmount() {
        return discountAmount;
    }

    public Integer getPaymentMethod() {
        return payType;
    }

    public void setPaymentMethod(Integer paymentMethod) {
        this.payType = paymentMethod;
    }

    public void setOrderNumber(String orderNumber) {
        this.orderSn = orderNumber;
    }

    public void setSubtotalAmount(BigDecimal subtotalAmount) {
        this.totalAmount = subtotalAmount;
    }

    public void setGrandTotalAmount(BigDecimal grandTotalAmount) {
        this.payAmount = grandTotalAmount;
    }

    public void setShippingAmount(BigDecimal shippingAmount) {
        this.freightAmount = shippingAmount;
    }

    public String getRecipientName() {
        return receiverName;
    }

    public void setRecipientName(String recipientName) {
        this.receiverName = recipientName;
    }

    public String getRecipientPhone() {
        return receiverPhone;
    }

    public void setRecipientPhone(String recipientPhone) {
        this.receiverPhone = recipientPhone;
    }

    public String getShippingPostalCode() {
        return receiverPostCode;
    }

    public void setShippingPostalCode(String shippingPostalCode) {
        this.receiverPostCode = shippingPostalCode;
    }

    public String getShippingAddressLine() {
        return receiverDetailAddress;
    }

    public void setShippingAddressLine(String shippingAddressLine) {
        this.receiverDetailAddress = shippingAddressLine;
    }

    @PrePersist
    protected void onCreate() {
        if (createTime == null) {
            createTime = OffsetDateTime.now();
        }
        if (totalAmount == null) {
            totalAmount = BigDecimal.ZERO;
        }
        if (payAmount == null) {
            payAmount = BigDecimal.ZERO;
        }
        if (freightAmount == null) {
            freightAmount = BigDecimal.ZERO;
        }
        if (promotionAmount == null) {
            promotionAmount = BigDecimal.ZERO;
        }
        if (couponAmount == null) {
            couponAmount = BigDecimal.ZERO;
        }
        if (discountAmount == null) {
            discountAmount = BigDecimal.ZERO;
        }
        if (payType == null) {
            payType = 0;
        }
        if (status == null) {
            status = 0;
        }
        if (orderType == null) {
            orderType = 0;
        }
        if (confirmStatus == null) {
            confirmStatus = false;
        }
        if (orderSn == null) {
            orderSn = "ORD-" + System.currentTimeMillis() + "-" + UUID.randomUUID().toString().substring(0, 8);
        }
    }

    @PreUpdate
    protected void onUpdate() {
        modifyTime = OffsetDateTime.now();
    }
}
