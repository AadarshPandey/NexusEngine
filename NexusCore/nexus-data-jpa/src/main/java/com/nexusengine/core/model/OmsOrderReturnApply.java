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
@Table(name = "oms_order_return_apply")
public class OmsOrderReturnApply implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "order_id")
    @Schema(title = "Order id")
    private Long orderId;

    @Column(name = "company_address_id")
    @Schema(title = "Company address id")
    private Long companyAddressId;

    @Column(name = "product_id")
    @Schema(title = "Product id")
    private Long productId;

    @Column(name = "order_sn")
    @Schema(title = "Order sn")
    private String orderSn;

    @Column(name = "create_time")
    @Schema(title = "Create time")
    private Date createTime;

    @Column(name = "member_username")
    @Schema(title = "Member username")
    private String memberUsername;

    @Column(name = "return_amount")
    @Schema(title = "Return amount")
    private BigDecimal returnAmount;

    @Column(name = "return_name")
    @Schema(title = "Return name")
    private String returnName;

    @Column(name = "return_phone")
    @Schema(title = "Return phone")
    private String returnPhone;

    @Schema(title = "Status")
    private Integer status;

    @Column(name = "handle_time")
    @Schema(title = "Handle time")
    private Date handleTime;

    @Column(name = "product_pic")
    @Schema(title = "Product pic")
    private String productPic;

    @Column(name = "product_name")
    @Schema(title = "Product name")
    private String productName;

    @Column(name = "product_brand")
    @Schema(title = "Product brand")
    private String productBrand;

    @Column(name = "product_attr")
    @Schema(title = "Product attr")
    private String productAttr;

    @Column(name = "product_count")
    @Schema(title = "Product count")
    private Integer productCount;

    @Column(name = "product_price")
    @Schema(title = "Product price")
    private BigDecimal productPrice;

    @Column(name = "product_real_price")
    @Schema(title = "Product real price")
    private BigDecimal productRealPrice;

    @Schema(title = "Reason")
    private String reason;

    @Schema(title = "Description")
    private String description;

    @Column(name = "proof_pics")
    @Schema(title = "Proof pics")
    private String proofPics;

    @Column(name = "handle_note")
    @Schema(title = "Handle note")
    private String handleNote;

    @Column(name = "handle_man")
    @Schema(title = "Handle man")
    private String handleMan;

    @Column(name = "receive_man")
    @Schema(title = "Receive man")
    private String receiveMan;

    @Column(name = "receive_time")
    @Schema(title = "Receive time")
    private Date receiveTime;

    @Column(name = "receive_note")
    @Schema(title = "Receive note")
    private String receiveNote;
}
