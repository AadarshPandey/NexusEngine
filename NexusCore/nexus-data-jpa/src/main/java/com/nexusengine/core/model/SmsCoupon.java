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
@Table(name = "coupon", schema = "promotion")
public class SmsCoupon implements Serializable {

    private static final long serialVersionUID = 1L;

        @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    
private Long id;

            @Schema(title = "Type")
    @Column(name = "discount_type")
    @org.hibernate.annotations.JdbcTypeCode(org.hibernate.type.SqlTypes.SMALLINT)
private Integer type;

    private String name;

    
            @Schema(title = "Count")
    @Column(name = "remaining_quantity")
private Integer count;

            @Schema(title = "Amount")
    @Column(name = "discount_value")
private BigDecimal amount;

        @Column(name = "per_member_limit")
    @Schema(title = "Per limit")
    @org.hibernate.annotations.JdbcTypeCode(org.hibernate.type.SqlTypes.SMALLINT)
private Integer perLimit;

        @Column(name = "min_spend_amount")
    @Schema(title = "Min point")
    
private BigDecimal minOrderAmount;

        @Column(name = "starts_at")
    
private OffsetDateTime startTime;

        @Column(name = "ends_at")
    
private OffsetDateTime endTime;

        @Column(name = "applicability_scope")
    @Schema(title = "Use type")
    @org.hibernate.annotations.JdbcTypeCode(org.hibernate.type.SqlTypes.SMALLINT)
private Integer useType;

    
    
    
        @Column(name = "claimed_count")
    @Schema(title = "Receive count")
    
private Integer receiveCount;

    
        @Schema(title = "Code")
    
private String code;

        @Column(name = "max_discount_amount")
    @Schema(title = "Max discount amount")
    
private BigDecimal maxDiscountAmount;

    @jakarta.persistence.Version
    private Integer version;
}
