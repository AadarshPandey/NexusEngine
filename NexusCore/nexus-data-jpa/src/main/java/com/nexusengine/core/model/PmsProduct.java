package com.nexusengine.core.model;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import java.io.Serializable;
import java.util.List;
import jakarta.persistence.OneToMany;
import jakarta.persistence.JoinColumn;
import java.math.BigDecimal;
import java.util.Date;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "pms_product")
public class PmsProduct implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "brand_id")
    private Long brandId;

    @Column(name = "product_category_id")
    private Long productCategoryId;

    @Column(name = "product_attribute_category_id")
    private Long productAttributeCategoryId;

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

    @Schema(title = "Sort")
    private Integer sort;

    @Schema(title = "Sale")
    private Integer sale;


    @Column(name = "gift_growth")
    @Schema(title = "Gift growth")
    private Integer giftGrowth;

    @Column(name = "gift_point")
    @Schema(title = "Gift point")
    private Integer giftPoint;

    @Column(name = "use_point_limit")
    @Schema(title = "Use point limit")
    private Integer usePointLimit;

    @Column(name = "sub_title")
    @Schema(title = "Sub title")
    private String subTitle;

    @Schema(title = "Unit")
    private String unit;

    @Schema(title = "Weight")
    private BigDecimal weight;

    @Column(name = "preview_status")
    @Schema(title = "Preview status")
    private Integer previewStatus;

    @Column(name = "service_ids")
    @Schema(title = "Service ids")
    private String serviceIds;

    private String keywords;

    private String note;
    @OneToMany(fetch = jakarta.persistence.FetchType.EAGER)
    @JoinColumn(name = "product_id")
    private List<PmsProductMedia> mediaList;


    @Column(name = "detail_title")
    private String detailTitle;

    @Column(name = "promotion_start_time")
    @Schema(title = "Promotion start time")
    private Date promotionStartTime;

    @Column(name = "promotion_end_time")
    @Schema(title = "Promotion end time")
    private Date promotionEndTime;

    @Column(name = "promotion_per_limit")
    @Schema(title = "Promotion per limit")
    private Integer promotionPerLimit;

    @Column(name = "promotion_type")
    @Schema(title = "Promotion type")
    private Integer promotionType;

    @Column(name = "brand_name")
    @Schema(title = "Brand name")
    private String brandName;

    @Column(name = "product_category_name")
    @Schema(title = "Product category name")
    private String productCategoryName;

    @Schema(title = "Description")
    private String description;

    @Column(name = "detail_desc")
    private String detailDesc;

    @Column(name = "detail_html")
    @Schema(title = "Detail html")
    private String detailHtml;

    @Column(name = "detail_mobile_html")
    @Schema(title = "Detail mobile html")
    private String detailMobileHtml;

    @Column(name = "vendor_id")
    @Schema(title = "Vendor ID")
    private Long vendorId;

    @org.hibernate.annotations.Formula("(SELECT COALESCE(MIN(s.price), 0) FROM pms_sku_stock s WHERE s.product_id = id)")
    private BigDecimal price;

    @org.hibernate.annotations.Formula("(SELECT COALESCE(MIN(s.price), 0) FROM pms_sku_stock s WHERE s.product_id = id)")
    private BigDecimal originalPrice;

    @org.hibernate.annotations.Formula("(SELECT COALESCE(SUM(s.stock), 0) FROM pms_sku_stock s WHERE s.product_id = id)")
    private Integer stock;

}
