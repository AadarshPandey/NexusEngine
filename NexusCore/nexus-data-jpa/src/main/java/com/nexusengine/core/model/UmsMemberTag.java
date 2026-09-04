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
@Table(name = "ums_member_tag")
public class UmsMemberTag implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @Column(name = "finish_order_count")
    @Schema(title = "Finish order count")
    private Integer finishOrderCount;

    @Column(name = "finish_order_amount")
    @Schema(title = "Finish order amount")
    private BigDecimal finishOrderAmount;
}
