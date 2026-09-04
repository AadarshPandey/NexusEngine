package com.nexusengine.core.model;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import java.io.Serializable;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "pms_product_category")
public class PmsProductCategory implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "parent_id")
    @Schema(title = "Parent id")
    private Long parentId;

    private String name;

    @Schema(title = "Level")
    private Integer level;

    @Column(name = "product_count")
    private Integer productCount;

    @Column(name = "product_unit")
    private String productUnit;

    @Column(name = "nav_status")
    @Schema(title = "Nav status")
    private Integer navStatus;

    @Column(name = "show_status")
    @Schema(title = "Show status")
    private Integer showStatus;

    private Integer sort;

    @Schema(title = "Icon")
    private String icon;

    private String keywords;

    @Schema(title = "Description")
    private String description;
}
