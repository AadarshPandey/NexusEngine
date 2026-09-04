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
@Table(name = "pms_feight_template")
public class PmsFeightTemplate implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @Column(name = "charge_type")
    @Schema(title = "Charge type")
    private Integer chargeType;

    @Column(name = "first_weight")
    @Schema(title = "First weight")
    private BigDecimal firstWeight;

    @Column(name = "first_fee")
    @Schema(title = "First fee")
    private BigDecimal firstFee;

    @Column(name = "continue_weight")
    private BigDecimal continueWeight;

    @Column(name = "continme_fee")
    private BigDecimal continmeFee;

    @Schema(title = "Dest")
    private String dest;
}
