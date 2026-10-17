package com.nexusengine.core.domain.ordering.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import com.nexusengine.core.domain.catalog.entity.Product;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import java.time.OffsetDateTime;
import java.math.BigDecimal;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "return_request", schema = "ordering")
public class ReturnRequest {
        @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)

private Long id;

        @Column(name = "order_id")
    @Schema(title = "Order id")

private Long orderId;
        @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id", insertable = false, updatable = false)

private CustomerOrder order;

        @Column(name = "return_address_id")
    @Schema(title = "Company address id")

private Long companyAddressId;
        @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "return_address_id", insertable = false, updatable = false)

private MerchantAddress companyAddress;

        @Column(name = "order_line_id")
    @Schema(title = "Product id")

private Long productId;
        @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_line_id", insertable = false, updatable = false)

private Product product;

            @Column(name = "order_sn")
    private String orderSn;

        @Column(name = "created_at")
    @Schema(title = "Create time")

private OffsetDateTime createTime;

    @Column(name = "member_username")
    private String memberUsername;

        @Column(name = "refund_amount")
    @Schema(title = "Return amount")

private BigDecimal returnAmount;

        @Column(name = "contact_name")
    @Schema(title = "Return name")

private String returnName;

        @Column(name = "contact_phone")
    @Schema(title = "Return phone")

private String returnPhone;

        @Schema(title = "Status")
    @JdbcTypeCode(SqlTypes.SMALLINT)
private Integer status;

        @Column(name = "reviewed_at")
    @Schema(title = "Handle time")

private OffsetDateTime handleTime;

            @Column(name = "product_pic")
    private String productPic;

            @Column(name = "product_name")
    private String productName;

            @Column(name = "product_brand")
    private String productBrand;

            @Column(name = "product_attr")
    private String productAttr;

        @Column(name = "quantity")
    @Schema(title = "Product count")

private Integer productCount;

        @Column(name = "product_price")
    @Schema(title = "Product price")

private BigDecimal productPrice;

        @Column(name = "unit_refund_amount")
    @Schema(title = "Product real price")

private BigDecimal productRealPrice;

            @Schema(title = "Reason")
    @Column(name = "reason_id")
private String reason;

            @Schema(title = "Description")
    @Column(name = "customer_comment")
private String description;

        @Column(name = "evidence_urls")
    @Schema(title = "Proof pics")

private String proofPics;

        @Column(name = "review_note")
    @Schema(title = "Handle note")

private String handleNote;

        @Column(name = "reviewed_by_admin_id")
    @Schema(title = "Handle man")

private String handleMan;

        @Column(name = "received_by_admin_id")
    @Schema(title = "Receive man")

private String receiveMan;

        @Column(name = "received_at")
    @Schema(title = "Receive time")

private OffsetDateTime receiveTime;

        @Column(name = "receipt_note")
    @Schema(title = "Receive note")

private String receiveNote;

    public Integer getQuantity() {
        return productCount;
    }

    public void setQuantity(Integer quantity) {
        this.productCount = quantity;
    }

    public BigDecimal getUnitRefundAmount() {
        return productRealPrice;
    }

    public void setUnitRefundAmount(BigDecimal unitRefundAmount) {
        this.productRealPrice = unitRefundAmount;
    }

    public BigDecimal getRefundAmount() {
        return returnAmount;
    }

    public void setRefundAmount(BigDecimal refundAmount) {
        this.returnAmount = refundAmount;
    }

    public String getContactName() {
        return returnName;
    }

    public void setContactName(String contactName) {
        this.returnName = contactName;
    }

    public String getContactPhone() {
        return returnPhone;
    }

    public void setContactPhone(String contactPhone) {
        this.returnPhone = contactPhone;
    }

    public String getOrderNumber() {
        return orderSn;
    }

    public void setOrderNumber(String orderNumber) {
        this.orderSn = orderNumber;
    }

    public String getProductImageUrl() {
        return productPic;
    }

    public void setProductImageUrl(String productImageUrl) {
        this.productPic = productImageUrl;
    }

    public String getBrandName() {
        return productBrand;
    }

    public void setBrandName(String brandName) {
        this.productBrand = brandName;
    }

    public String getSkuAttributes() {
        return productAttr;
    }

    public void setSkuAttributes(String skuAttributes) {
        this.productAttr = skuAttributes;
    }

    public String getCustomerComment() {
        return description;
    }

    public void setCustomerComment(String customerComment) {
        this.description = customerComment;
    }
}
