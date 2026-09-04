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
@Table(name = "ums_member_rule_setting")
public class UmsMemberRuleSetting implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "continue_sign_day")
    @Schema(title = "Continue sign day")
    private Integer continueSignDay;

    @Column(name = "continue_sign_point")
    @Schema(title = "Continue sign point")
    private Integer continueSignPoint;

    @Column(name = "consume_per_point")
    @Schema(title = "Consume per point")
    private BigDecimal consumePerPoint;

    @Column(name = "low_order_amount")
    @Schema(title = "Low order amount")
    private BigDecimal lowOrderAmount;

    @Column(name = "max_point_per_order")
    @Schema(title = "Max point per order")
    private Integer maxPointPerOrder;

    @Schema(title = "Type")
    private Integer type;
}
