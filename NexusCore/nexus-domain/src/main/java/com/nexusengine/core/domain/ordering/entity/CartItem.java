package com.nexusengine.core.domain.ordering.entity;

import com.nexusengine.core.domain.catalog.entity.Product;
import com.nexusengine.core.domain.catalog.entity.Category;
import com.nexusengine.core.domain.customer.entity.Member;

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
@Table(name = "cart_item", schema = "ordering")
public class CartItem implements Serializable {

    private static final long serialVersionUID = 1L;

        @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    
private Long id;

        @Column(name = "product_id")
    
private Long productId;
        @com.fasterxml.jackson.annotation.JsonIgnore
    @jakarta.persistence.ManyToOne(fetch = jakarta.persistence.FetchType.LAZY)
    @jakarta.persistence.JoinColumn(name = "product_id", insertable = false, updatable = false)
    
private Product product;

        @Column(name = "sku_id")
    
private Long productSkuId;

        @Column(name = "member_id")
    
private Long memberId;
        @com.fasterxml.jackson.annotation.JsonIgnore
    @jakarta.persistence.ManyToOne(fetch = jakarta.persistence.FetchType.LAZY)
    @jakarta.persistence.JoinColumn(name = "member_id", insertable = false, updatable = false)
    
private Member member;

        @Schema(title = "Quantity")
    
private Integer quantity;

            @Schema(title = "Price")
    @Column(name = "unit_price_at_add")
private BigDecimal price;

            @org.hibernate.annotations.Formula("(SELECT p.primary_image_url FROM catalog.product p WHERE p.id = product_id)")
    private String productPic;

            @org.hibernate.annotations.Formula("(SELECT p.name FROM catalog.product p WHERE p.id = product_id)")
    private String productName;

            @org.hibernate.annotations.Formula("(SELECT s.sku_code FROM catalog.product_sku s WHERE s.id = sku_id)")
    private String productSkuCode;

        @Column(name = "created_at")
    @Schema(title = "Create date")
    
private OffsetDateTime createDate;

        @Column(name = "updated_at")
    @Schema(title = "Modify date")
    
private OffsetDateTime modifyDate;

        @Transient
    @Schema(title = "Delete status")
    
private Integer deleteStatus;

            @org.hibernate.annotations.Formula("(SELECT p.category_id FROM catalog.product p WHERE p.id = product_id)")
    private Long productCategoryId;
    @Transient
private Category productCategory;

            @org.hibernate.annotations.Formula("(SELECT b.name FROM catalog.brand b JOIN catalog.product p ON p.brand_id = b.id WHERE p.id = product_id)")
    private String productBrand;

            @org.hibernate.annotations.Formula("(SELECT p.product_code FROM catalog.product p WHERE p.id = product_id)")
    private String productSn;

            @org.hibernate.annotations.Formula("(SELECT s.sku_attributes FROM catalog.product_sku s WHERE s.id = sku_id)")
    private String productAttr;
}
