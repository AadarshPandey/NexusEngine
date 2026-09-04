package com.nexusengine.core.model;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "sms_coupon")
public class SmsCoupon implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Schema(title = "Type")
    private Integer type;

    private String name;

    @Schema(title = "Platform")
    private Integer platform;

    @Schema(title = "Count")
    private Integer count;

    @Schema(title = "Amount")
    private BigDecimal amount;

    @Column(name = "per_limit")
    @Schema(title = "Per limit")
    private Integer perLimit;

    @Column(name = "min_point")
    @Schema(title = "Min point")
    private BigDecimal minPoint;

    @Column(name = "start_time")
    private Date startTime;

    @Column(name = "end_time")
    private Date endTime;

    @Column(name = "use_type")
    @Schema(title = "Use type")
    private Integer useType;

    @Schema(title = "Note")
    private String note;

    @Column(name = "publish_count")
    @Schema(title = "Publish count")
    private Integer publishCount;

    @Column(name = "use_count")
    @Schema(title = "Use count")
    private Integer useCount;

    @Column(name = "receive_count")
    @Schema(title = "Receive count")
    private Integer receiveCount;

    @Column(name = "enable_time")
    @Schema(title = "Enable time")
    private Date enableTime;

    @Schema(title = "Code")
    private String code;

    @Column(name = "member_level")
    @Schema(title = "Member level")
    private Integer memberLevel;
}
