package com.nexusengine.core.model;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import java.io.Serializable;
import java.util.List;
import jakarta.persistence.OneToMany;
import jakarta.persistence.JoinColumn;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "pms_product", schema = "pms")
public class PmsProduct implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "brand_id")
    private Long brandId;
    @com.fasterxml.jackson.annotation.JsonIgnore
    @jakarta.persistence.ManyToOne(fetch = jakarta.persistence.FetchType.LAZY)
    @jakarta.persistence.JoinColumn(name = "brand_id", insertable = false, updatable = false)
    private PmsBrand brand;

    @Column(name = "product_category_id")
    private Long productCategoryId;
    @com.fasterxml.jackson.annotation.JsonIgnore
    @jakarta.persistence.ManyToOne(fetch = jakarta.persistence.FetchType.LAZY)
    @jakarta.persistence.JoinColumn(name = "product_category_id", insertable = false, updatable = false)
    private PmsProductCategory productCategory;

    @Column(name = "product_attribute_category_id")
    private Long productAttributeCategoryId;
    @com.fasterxml.jackson.annotation.JsonIgnore
    @jakarta.persistence.ManyToOne(fetch = jakarta.persistence.FetchType.LAZY)
    @jakarta.persistence.JoinColumn(name = "product_attribute_category_id", insertable = false, updatable = false)
    private PmsProductAttributeCategory productAttributeCategory;

    private String name;

    private String pic;

    @Column(name = "product_sn")
    @Schema(title = "Product sn")
    private String productSn;

    @Column(name = "delete_status")
    @Schema(title = "Delete status")
    private Integer deleteStatus;

    @Column(name = "publish_status")
    @Schema(title = "Publish status")
    private Integer publishStatus;

    @Column(name = "new_status")
    @Schema(title = "New status")
    private Integer newStatus;

    @Column(name = "recommend_status")
    @Schema(title = "Recommend status")
    private Integer recommendStatus;

    @Column(name = "verify_status")
    @Schema(title = "Verify status")
    private Integer verifyStatus;

    @Schema(title = "Sale")
    private Integer sale;

    private String keywords;
    @OneToMany(fetch = jakarta.persistence.FetchType.EAGER)
    @JoinColumn(name = "product_id")
    @com.fasterxml.jackson.annotation.JsonManagedReference
    private List<PmsProductGallery> galleryList;



    @Column(name = "promotion_type")
    @Schema(title = "Promotion type")
    private Integer promotionType;

    @Column(name = "brand_name")
    @Schema(title = "Brand name")
    private String brandName;

    @Schema(title = "Description")
    private String description;

    @org.hibernate.annotations.Formula("(SELECT COALESCE(MIN(s.price), 0) FROM catalog.pms_sku_stock s WHERE s.product_id = id)")
    private BigDecimal price;

    @org.hibernate.annotations.Formula("(SELECT COALESCE(MIN(s.price), 0) FROM catalog.pms_sku_stock s WHERE s.product_id = id)")
    private BigDecimal originalPrice;

    @org.hibernate.annotations.Formula("(SELECT COALESCE(SUM(s.stock), 0) FROM catalog.pms_sku_stock s WHERE s.product_id = id)")
    private Integer stock;

}
