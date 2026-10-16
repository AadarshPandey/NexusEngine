package com.nexusengine.core.domain.ordering.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.Formula;

import com.nexusengine.core.domain.catalog.entity.Product;
import com.nexusengine.core.domain.catalog.entity.Category;
import com.nexusengine.core.domain.customer.entity.Member;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import java.time.OffsetDateTime;
import java.math.BigDecimal;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "cart_item", schema = "ordering")
public class CartItem {
        @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)

private Long id;

        @Column(name = "product_id")

private Long productId;
        @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", insertable = false, updatable = false)

private Product product;

        @Column(name = "sku_id")

private Long productSkuId;

        @Column(name = "member_id")

private Long memberId;
        @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", insertable = false, updatable = false)

private Member member;

        @Schema(title = "Quantity")

private Integer quantity;

            @Schema(title = "Price")
    @Column(name = "unit_price_at_add")
private BigDecimal price;

            @Formula("(SELECT p.primary_image_url FROM catalog.product p WHERE p.id = product_id)")
    private String productPic;

            @Formula("(SELECT p.name FROM catalog.product p WHERE p.id = product_id)")
    private String productName;

            @Formula("(SELECT s.sku_code FROM catalog.product_sku s WHERE s.id = sku_id)")
    private String productSkuCode;

        @Column(name = "created_at")
    @Schema(title = "Create date")
    private OffsetDateTime createDate = OffsetDateTime.now();

        @Column(name = "updated_at")
    @Schema(title = "Modify date")
    private OffsetDateTime modifyDate = OffsetDateTime.now();

    @PrePersist
    protected void onCreate() {
        if (createDate == null) {
            createDate = OffsetDateTime.now();
        }
        if (modifyDate == null) {
            modifyDate = OffsetDateTime.now();
        }
    }

    @PreUpdate
    protected void onUpdate() {
        modifyDate = OffsetDateTime.now();
    }

        @Transient
    @Schema(title = "Delete status")

private Integer deleteStatus;

            @Formula("(SELECT p.category_id FROM catalog.product p WHERE p.id = product_id)")
    private Long productCategoryId;
    @Transient
private Category productCategory;

            @Formula("(SELECT b.name FROM catalog.brand b JOIN catalog.product p ON p.brand_id = b.id WHERE p.id = product_id)")
    private String productBrand;

            @Formula("(SELECT p.product_code FROM catalog.product p WHERE p.id = product_id)")
    private String productSn;

            @Formula("(SELECT s.sku_attributes FROM catalog.product_sku s WHERE s.id = sku_id)")
    private String productAttr;

    public String getPrimaryImageUrl() {
        return productPic;
    }

    public String getName() {
        return productName;
    }

    public String getSkuCode() {
        return productSkuCode;
    }

    public String getProductCode() {
        return productSn;
    }

    public BigDecimal getCurrentListPrice() {
        return price;
    }

    public Long getSkuId() {
        return productSkuId;
    }
}
