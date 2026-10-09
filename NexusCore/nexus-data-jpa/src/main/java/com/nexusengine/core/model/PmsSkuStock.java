package com.nexusengine.core.model;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "product_sku", schema = "catalog")
public class PmsSkuStock implements Serializable {

    private static final long serialVersionUID = 1L;

        @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    
private Long id;

        @Column(name = "product_id")
    
private Long productId;
        @com.fasterxml.jackson.annotation.JsonIgnore
    @jakarta.persistence.ManyToOne(fetch = jakarta.persistence.FetchType.LAZY)
    @jakarta.persistence.JoinColumn(name = "product_id", insertable = false, updatable = false)
    
private PmsProduct product;

        @Column(name = "sku_code")
    @Schema(title = "Sku code")
    
private String skuCode;

            @Column(name = "list_price")
private BigDecimal price;

            @Schema(title = "Stock")
    @Column(name = "on_hand_qty")
private Integer stock;

        @Column(name = "low_stock_threshold")
    @Schema(title = "Low stock")
    
private Integer lowStock;

            @Schema(title = "Pic")
    @Column(name = "image_url")
private String pic;

            @Schema(title = "Sale")
    @Column(name = "units_sold")
private Integer sale;

        @Column(name = "sale_price")
    @Schema(title = "Promotion price")
    
private BigDecimal promotionPrice;

        @Column(name = "reserved_qty")
    @Schema(title = "Lock stock")
    
private Integer lockStock;

        @Schema(title = "Sp data")
    @Column(name = "sku_attributes", columnDefinition = "jsonb")
    @org.hibernate.annotations.JdbcTypeCode(org.hibernate.type.SqlTypes.JSON)
    
private String spData;

    @jakarta.persistence.Version
    private Integer version;
}
