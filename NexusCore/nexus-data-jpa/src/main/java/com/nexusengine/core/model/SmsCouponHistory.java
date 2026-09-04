package com.nexusengine.core.model;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import java.io.Serializable;
import java.util.Date;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "sms_coupon_history")
public class SmsCouponHistory implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "coupon_id")
    private Long couponId;

    @Column(name = "member_id")
    private Long memberId;

    @Column(name = "coupon_code")
    private String couponCode;

    @Column(name = "member_nickname")
    @Schema(title = "Member nickname")
    private String memberNickname;

    @Column(name = "get_type")
    @Schema(title = "Get type")
    private Integer getType;

    @Column(name = "create_time")
    private Date createTime;

    @Column(name = "use_status")
    @Schema(title = "Use status")
    private Integer useStatus;

    @Column(name = "use_time")
    @Schema(title = "Use time")
    private Date useTime;

    @Column(name = "order_id")
    @Schema(title = "Order id")
    private Long orderId;

    @Column(name = "order_sn")
    @Schema(title = "Order sn")
    private String orderSn;
}
