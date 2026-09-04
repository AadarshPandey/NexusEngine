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
@Table(name = "oms_order_return_reason")
public class OmsOrderReturnReason implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Schema(title = "Name")
    private String name;

    private Integer sort;

    @Schema(title = "Status")
    private Integer status;

    @Column(name = "create_time")
    @Schema(title = "Create time")
    private Date createTime;
}
