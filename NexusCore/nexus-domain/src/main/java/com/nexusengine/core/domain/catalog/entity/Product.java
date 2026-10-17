package com.nexusengine.core.domain.catalog.entity;

import jakarta.persistence.FetchType;
import org.hibernate.type.SqlTypes;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import jakarta.persistence.Version;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Formula;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.annotations.SQLRestriction;
import org.hibernate.type.SqlTypes;

@Getter
@Setter
@Entity
@Table(name = "product", schema = "catalog")
@SQLRestriction("deleted_at IS NULL")
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "brand_id")
    private Long brandId;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "brand_id", insertable = false, updatable = false)
    private Brand brand;

    @Column(name = "category_id")
    private Long productCategoryId;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id", insertable = false, updatable = false)
    private Category productCategory;

    @Column(name = "product_type_id")
    private Long productAttributeCategoryId;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_type_id", insertable = false, updatable = false)
    private ProductType productAttributeCategory;

    private String name;

    @Column(name = "primary_image_url")
    private String primaryImageUrl;

    @Column(name = "product_code")
    @Schema(title = "Product code")
    private String productCode;

    @Column(name = "deleted_at")
    @Schema(title = "Deleted at")
    private Instant deletedAt;

    @Column(name = "is_published")
    @Schema(title = "Publish status")
    private Boolean isPublished;

    @Column(name = "is_new_arrival")
    @Schema(title = "New arrival status")
    private Boolean isNewArrival;

    @Column(name = "is_featured")
    @Schema(title = "Featured status")
    private Boolean isFeatured;

    @Column(name = "approval_status")
    @Schema(title = "Approval status")
    @JdbcTypeCode(SqlTypes.SMALLINT)
    private Integer approvalStatus;

    @Schema(title = "Units sold")
    @Column(name = "units_sold")
    private Integer unitsSold;

    @Column(name = "search_keywords")
    private String keywords;

    @OneToMany(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id")
    @JsonManagedReference
    private List<ProductMedia> galleryList;

    @Column(name = "promotion_type")
    @Schema(title = "Promotion type")
    @JdbcTypeCode(SqlTypes.SMALLINT)
    private Integer promotionType;

    @Schema(title = "List Price")
    @Formula("(SELECT COALESCE(min(s.list_price), 0) FROM catalog.product_sku s WHERE s.product_id = id)")
    private BigDecimal listPrice;

    @Schema(title = "On-hand quantity")
    @Formula("(SELECT COALESCE(sum(s.on_hand_qty), 0) FROM catalog.product_sku s WHERE s.product_id = id)")
    private Integer onHandQty;

    @Formula("(SELECT b.name FROM catalog.brand b WHERE b.id = brand_id)")
    private String brandName;

    @Schema(title = "Description")
    private String description;

    @Version
    private Integer version;

    public BigDecimal getPrice() {
        return listPrice != null ? listPrice : BigDecimal.ZERO;
    }

    @PrePersist
    protected void onCreate() {
        if (isPublished == null) {
            isPublished = true;
        }
        if (isNewArrival == null) {
            isNewArrival = false;
        }
        if (isFeatured == null) {
            isFeatured = false;
        }
        if (approvalStatus == null) {
            approvalStatus = 1;
        }
        if (unitsSold == null) {
            unitsSold = 0;
        }
        if (promotionType == null) {
            promotionType = 0;
        }
        if (productCode == null) {
            productCode = "PROD-" + System.currentTimeMillis();
        }
    }
}
