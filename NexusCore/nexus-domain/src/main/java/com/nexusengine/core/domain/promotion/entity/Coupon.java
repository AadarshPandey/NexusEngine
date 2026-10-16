package com.nexusengine.core.domain.promotion.entity;

import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import java.time.OffsetDateTime;
import java.math.BigDecimal;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "coupon", schema = "promotion")
public class Coupon {
        @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)

private Long id;

            @Schema(title = "Type")
    @Column(name = "discount_type")
    @JdbcTypeCode(SqlTypes.SMALLINT)
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
    @JdbcTypeCode(SqlTypes.SMALLINT)
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
    @JdbcTypeCode(SqlTypes.SMALLINT)
private Integer useType;




        @Column(name = "claimed_count")
    @Schema(title = "Receive count")

private Integer receiveCount;


        @Schema(title = "Code")

private String code;

        @Column(name = "max_discount_amount")
    @Schema(title = "Max discount amount")

private BigDecimal maxDiscountAmount;

    @Version
    private Integer version;

    @PrePersist
    protected void onCreate() {
        if (receiveCount == null) {
            receiveCount = 0;
        }
        if (version == null) {
            version = 0;
        }
        if (perLimit == null) {
            perLimit = 1;
        }
        if (useType == null) {
            useType = 0;
        }
        if (amount == null) {
            amount = BigDecimal.ZERO;
        }
        if (minOrderAmount == null) {
            minOrderAmount = BigDecimal.ZERO;
        }
    }
}
