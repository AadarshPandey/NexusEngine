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
@Table(name = "oms_order_operate_history")
public class OmsOrderOperateHistory implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "order_id")
    @Schema(title = "Order id")
    private Long orderId;

    @Column(name = "operate_man")
    @Schema(title = "Operate man")
    private String operateMan;

    @Column(name = "create_time")
    @Schema(title = "Create time")
    private Date createTime;

    @Column(name = "order_status")
    @Schema(title = "Order status")
    private Integer orderStatus;

    @Schema(title = "Note")
    private String note;
}
