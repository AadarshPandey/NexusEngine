package com.nexusengine.core.model;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import java.io.Serializable;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "ums_integration_consume_setting")
public class UmsIntegrationConsumeSetting implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "deduction_per_amount")
    @Schema(title = "Deduction per amount")
    private Integer deductionPerAmount;

    @Column(name = "max_percent_per_order")
    @Schema(title = "Max percent per order")
    private Integer maxPercentPerOrder;

    @Column(name = "use_unit")
    @Schema(title = "Use unit")
    private Integer useUnit;

    @Column(name = "coupon_status")
    @Schema(title = "Coupon status")
    private Integer couponStatus;
}
