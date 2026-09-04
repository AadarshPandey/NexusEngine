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
@Table(name = "pms_product_operate_log")
public class PmsProductOperateLog implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "product_id")
    private Long productId;

    @Column(name = "price_old")
    private BigDecimal priceOld;

    @Column(name = "price_new")
    private BigDecimal priceNew;

    @Column(name = "sale_price_old")
    private BigDecimal salePriceOld;

    @Column(name = "sale_price_new")
    private BigDecimal salePriceNew;

    @Column(name = "gift_point_old")
    @Schema(title = "Gift point old")
    private Integer giftPointOld;

    @Column(name = "gift_point_new")
    private Integer giftPointNew;

    @Column(name = "use_point_limit_old")
    private Integer usePointLimitOld;

    @Column(name = "use_point_limit_new")
    private Integer usePointLimitNew;

    @Column(name = "operate_man")
    @Schema(title = "Operate man")
    private String operateMan;

    @Column(name = "create_time")
    private Date createTime;
}
