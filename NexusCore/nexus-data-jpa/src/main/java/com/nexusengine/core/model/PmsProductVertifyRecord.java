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
@Table(name = "pms_product_vertify_record")
public class PmsProductVertifyRecord implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "product_id")
    private Long productId;

    @Column(name = "create_time")
    private Date createTime;

    @Column(name = "vertify_man")
    @Schema(title = "Vertify man")
    private String vertifyMan;

    private Integer status;

    @Schema(title = "Detail")
    private String detail;
}
