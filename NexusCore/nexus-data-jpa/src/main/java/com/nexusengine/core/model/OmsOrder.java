package com.nexusengine.core.model;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "oms_order")
public class OmsOrder implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(title = "Id")
    private Long id;

    @Column(name = "member_id")
    private Long memberId;

    @Column(name = "coupon_id")
    private Long couponId;

    @Column(name = "order_sn")
    @Schema(title = "Order sn")
    private String orderSn;

    @Column(name = "create_time")
    @Schema(title = "Create time")
    private Date createTime;

    @Column(name = "member_username")
    @Schema(title = "Member username")
    private String memberUsername;

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

    @Column(name = "integration_amount")
    @Schema(title = "Integration amount")
    private BigDecimal integrationAmount;

    @Column(name = "coupon_amount")
    @Schema(title = "Coupon amount")
    private BigDecimal couponAmount;

    @Column(name = "discount_amount")
    @Schema(title = "Discount amount")
    private BigDecimal discountAmount;

    @Column(name = "pay_type")
    @Schema(title = "Pay type")
    private Integer payType;

    @Column(name = "source_type")
    @Schema(title = "Source type")
    private Integer sourceType;

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

    @Column(name = "auto_confirm_day")
    @Schema(title = "Auto confirm day")
    private Integer autoConfirmDay;

    @Schema(title = "Integration")
    private Integer integration;

    @Schema(title = "Growth")
    private Integer growth;

    @Column(name = "promotion_info")
    @Schema(title = "Promotion info")
    private String promotionInfo;

    @Column(name = "bill_type")
    @Schema(title = "Bill type")
    private Integer billType;

    @Column(name = "bill_header")
    @Schema(title = "Bill header")
    private String billHeader;

    @Column(name = "bill_content")
    @Schema(title = "Bill content")
    private String billContent;

    @Column(name = "bill_receiver_phone")
    @Schema(title = "Bill receiver phone")
    private String billReceiverPhone;

    @Column(name = "bill_receiver_email")
    @Schema(title = "Bill receiver email")
    private String billReceiverEmail;

    @Column(name = "receiver_name")
    @Schema(title = "Receiver name")
    private String receiverName;

    @Column(name = "receiver_phone")
    @Schema(title = "Receiver phone")
    private String receiverPhone;

    @Column(name = "receiver_post_code")
    @Schema(title = "Receiver post code")
    private String receiverPostCode;

    @Column(name = "receiver_province")
    @Schema(title = "Receiver province")
    private String receiverProvince;

    @Column(name = "receiver_city")
    @Schema(title = "Receiver city")
    private String receiverCity;

    @Column(name = "receiver_region")
    @Schema(title = "Receiver region")
    private String receiverRegion;

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

    @Column(name = "use_integration")
    @Schema(title = "Use integration")
    private Integer useIntegration;

    @Column(name = "payment_time")
    @Schema(title = "Payment time")
    private Date paymentTime;

    @Column(name = "delivery_time")
    @Schema(title = "Delivery time")
    private Date deliveryTime;

    @Column(name = "receive_time")
    @Schema(title = "Receive time")
    private Date receiveTime;

    @Column(name = "comment_time")
    @Schema(title = "Comment time")
    private Date commentTime;

    @Column(name = "modify_time")
    @Schema(title = "Modify time")
    private Date modifyTime;

    @Column(name = "vendor_id")
    @Schema(title = "Vendor ID")
    private Long vendorId;
}
