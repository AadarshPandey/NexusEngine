package com.nexusengine.core.model;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "sms_coupon", schema = "marketing")
public class SmsCoupon implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Schema(title = "Type")
    private Integer type;

    private String name;

    
    @Schema(title = "Count")
    private Integer count;

    @Schema(title = "Amount")
    private BigDecimal amount;

    @Column(name = "per_limit")
    @Schema(title = "Per limit")
    private Integer perLimit;

    @Column(name = "min_order_amount")
    @Schema(title = "Min point")
    private BigDecimal minOrderAmount;

    @Column(name = "start_time")
    private LocalDateTime startTime;

    @Column(name = "end_time")
    private LocalDateTime endTime;

    @Column(name = "use_type")
    @Schema(title = "Use type")
    private Integer useType;

    
    
    
    @Column(name = "receive_count")
    @Schema(title = "Receive count")
    private Integer receiveCount;

    
    @Schema(title = "Code")
    private String code;

    @Column(name = "max_discount_amount")
    @Schema(title = "Max discount amount")
    private BigDecimal maxDiscountAmount;
}
