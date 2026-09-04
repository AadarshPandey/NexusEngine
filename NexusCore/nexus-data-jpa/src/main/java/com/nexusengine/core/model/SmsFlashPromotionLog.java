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
@Table(name = "sms_flash_promotion_log")
public class SmsFlashPromotionLog implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "member_id")
    private Integer memberId;

    @Column(name = "product_id")
    private Long productId;

    @Column(name = "member_phone")
    private String memberPhone;

    @Column(name = "product_name")
    private String productName;

    @Column(name = "subscribe_time")
    @Schema(title = "Subscribe time")
    private Date subscribeTime;

    @Column(name = "send_time")
    private Date sendTime;
}
