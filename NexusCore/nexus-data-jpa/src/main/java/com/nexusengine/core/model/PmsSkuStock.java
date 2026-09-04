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
@Table(name = "pms_sku_stock")
public class PmsSkuStock implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "product_id")
    private Long productId;

    @Column(name = "sku_code")
    @Schema(title = "Sku code")
    private String skuCode;

    private BigDecimal price;

    @Schema(title = "Stock")
    private Integer stock;

    @Column(name = "low_stock")
    @Schema(title = "Low stock")
    private Integer lowStock;

    @Schema(title = "Pic")
    private String pic;

    @Schema(title = "Sale")
    private Integer sale;

    @Column(name = "promotion_price")
    @Schema(title = "Promotion price")
    private BigDecimal promotionPrice;

    @Column(name = "lock_stock")
    @Schema(title = "Lock stock")
    private Integer lockStock;

    @Column(name = "sp_data")
    @Schema(title = "Sp data")
    private String spData;
}
