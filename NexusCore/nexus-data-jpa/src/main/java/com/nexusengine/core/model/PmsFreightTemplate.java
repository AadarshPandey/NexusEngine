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
@Table(name = "pms_freight_template")
public class PmsFreightTemplate implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @Column(name = "charge_type")
    @Schema(title = "Charge type")
    private Integer chargeType;

    @Column(name = "base_weight")
    @Schema(title = "First weight")
    private BigDecimal baseWeight;

    @Column(name = "base_shipping_fee")
    @Schema(title = "First fee")
    private BigDecimal baseShippingFee;

    @Column(name = "incremental_weight_unit")
    private BigDecimal incrementalWeightUnit;

    @Column(name = "incremental_fee")
    private BigDecimal incrementalFee;

    @Schema(title = "Dest")
    private String dest;
}
