package com.nexusengine.core.model;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "pms_member_price")
public class PmsMemberPrice implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "product_id")
    private Long productId;

    @Column(name = "member_level_id")
    private Long memberLevelId;

    @Column(name = "member_price")
    @Schema(title = "Member price")
    private BigDecimal memberPrice;

    @Column(name = "member_level_name")
    private String memberLevelName;
}
