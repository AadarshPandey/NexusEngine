package com.nexusengine.core.domain.catalog.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import java.time.OffsetDateTime;
import java.math.BigDecimal;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "product_price_change", schema = "catalog")
public class ProductPriceChange {
        @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)

private Long id;

        @Column(name = "product_id")

private Long productId;
        @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", insertable = false, updatable = false)

private Product product;

        @Column(name = "old_list_price")

private BigDecimal priceOld;

        @Column(name = "new_list_price")

private BigDecimal priceNew;

        @Column(name = "old_sale_price")

private BigDecimal salePriceOld;

        @Column(name = "new_sale_price")

private BigDecimal salePriceNew;





        @Column(name = "changed_by_admin_id")
    @Schema(title = "Operate man")

private String operateMan;

        @Column(name = "changed_at")

private OffsetDateTime createTime;
}
