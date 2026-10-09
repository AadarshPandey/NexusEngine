package com.nexusengine.core.model;

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
@Table(name = "product_price_change", schema = "catalog")
public class PmsProductAuditLog implements Serializable {

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
