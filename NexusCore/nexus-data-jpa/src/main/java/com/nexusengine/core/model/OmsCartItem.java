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
@Table(name = "oms_cart_item")
public class OmsCartItem implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "product_id")
    private Long productId;

    @Column(name = "product_sku_id")
    private Long productSkuId;

    @Column(name = "member_id")
    private Long memberId;

    @Schema(title = "Quantity")
    private Integer quantity;

    @Schema(title = "Price")
    private BigDecimal price;

    @Column(name = "product_pic")
    @Schema(title = "Product pic")
    private String productPic;

    @Column(name = "product_name")
    @Schema(title = "Product name")
    private String productName;

    @Column(name = "product_sub_title")
    @Schema(title = "Product sub title")
    private String productSubTitle;

    @Column(name = "product_sku_code")
    @Schema(title = "Product sku code")
    private String productSkuCode;

    @Column(name = "member_nickname")
    @Schema(title = "Member nickname")
    private String memberNickname;

    @Column(name = "create_date")
    @Schema(title = "Create date")
    private Date createDate;

    @Column(name = "modify_date")
    @Schema(title = "Modify date")
    private Date modifyDate;

    @Column(name = "delete_status")
    @Schema(title = "Delete status")
    private Integer deleteStatus;

    @Column(name = "product_category_id")
    @Schema(title = "Product category id")
    private Long productCategoryId;

    @Column(name = "product_brand")
    private String productBrand;

    @Column(name = "product_sn")
    private String productSn;

    @Column(name = "product_attr")
    @Schema(title = "Product attr")
    private String productAttr;
}
