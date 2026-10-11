package com.nexusengine.core.domain.catalog.entity;


import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import java.io.Serializable;
import java.util.List;
import jakarta.persistence.OneToMany;
import jakarta.persistence.JoinColumn;
import java.math.BigDecimal;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "product", schema = "catalog")
public class Product implements Serializable {

    private static final long serialVersionUID = 1L;

        @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)

private Long id;
        @Column(name = "brand_id")

private Long brandId;
        @com.fasterxml.jackson.annotation.JsonIgnore
    @jakarta.persistence.ManyToOne(fetch = jakarta.persistence.FetchType.LAZY)
    @jakarta.persistence.JoinColumn(name = "brand_id", insertable = false, updatable = false)

private Brand brand;

        @Column(name = "category_id")

private Long productCategoryId;
        @com.fasterxml.jackson.annotation.JsonIgnore
    @jakarta.persistence.ManyToOne(fetch = jakarta.persistence.FetchType.LAZY)
    @jakarta.persistence.JoinColumn(name = "category_id", insertable = false, updatable = false)

private Category productCategory;

        @Column(name = "product_type_id")

private Long productAttributeCategoryId;
        @com.fasterxml.jackson.annotation.JsonIgnore
    @jakarta.persistence.ManyToOne(fetch = jakarta.persistence.FetchType.LAZY)
    @jakarta.persistence.JoinColumn(name = "product_type_id", insertable = false, updatable = false)

private ProductType productAttributeCategory;

    private String name;

    @com.fasterxml.jackson.annotation.JsonProperty("primaryImageUrl")
            @Column(name = "primary_image_url")
private String primaryImageUrl;

        @Column(name = "product_code")
    @Schema(title = "Product sn")

@com.fasterxml.jackson.annotation.JsonProperty("productCode")
    private String productSn;

        @Convert(converter = com.nexusengine.core.domain.shared.converter.IntegerToDeletedAtConverter.class)
    @Column(name = "deleted_at")
    @Schema(title = "Delete status")

private Integer deleteStatus;

        @Column(name = "is_published")
    @Schema(title = "Publish status")
    @jakarta.persistence.Convert(converter = com.nexusengine.core.domain.shared.converter.IntegerToBooleanConverter.class)
@com.fasterxml.jackson.annotation.JsonProperty("isPublished")
    private Integer publishStatus;

        @Column(name = "is_new_arrival")
    @Schema(title = "New status")
    @jakarta.persistence.Convert(converter = com.nexusengine.core.domain.shared.converter.IntegerToBooleanConverter.class)
@com.fasterxml.jackson.annotation.JsonProperty("isNewArrival")
    private Integer newStatus;

        @Column(name = "is_featured")
    @Schema(title = "Recommend status")
    @jakarta.persistence.Convert(converter = com.nexusengine.core.domain.shared.converter.IntegerToBooleanConverter.class)
@com.fasterxml.jackson.annotation.JsonProperty("isFeatured")
    private Integer recommendStatus;

        @Column(name = "approval_status")
    @Schema(title = "Verify status")
    @org.hibernate.annotations.JdbcTypeCode(org.hibernate.type.SqlTypes.SMALLINT)
@com.fasterxml.jackson.annotation.JsonProperty("approvalStatus")
    private Integer verifyStatus;

            @Schema(title = "Sale")
    @Column(name = "units_sold")
@com.fasterxml.jackson.annotation.JsonProperty("unitsSold")
    private Integer sale;

            @Column(name = "search_keywords")
private String keywords;
        @OneToMany(fetch = jakarta.persistence.FetchType.EAGER)
    @JoinColumn(name = "product_id")
    @com.fasterxml.jackson.annotation.JsonManagedReference

private List<ProductMedia> galleryList;



        @Column(name = "promotion_type")
    @Schema(title = "Promotion type")
    @org.hibernate.annotations.JdbcTypeCode(org.hibernate.type.SqlTypes.SMALLINT)
private Integer promotionType;

    @Schema(title = "List Price")
    @org.hibernate.annotations.Formula("(SELECT COALESCE(min(s.list_price), 0) FROM catalog.product_sku s WHERE s.product_id = id)")
    @com.fasterxml.jackson.annotation.JsonProperty("listPrice")
    private BigDecimal listPrice;


            @org.hibernate.annotations.Formula("(SELECT b.name FROM catalog.brand b WHERE b.id = brand_id)")
    private String brandName;

        @Schema(title = "Description")

private String description;


    @jakarta.persistence.Version
    private Integer version;
}
