package com.nexusengine.core.model;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import java.io.Serializable;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "pms_product_attribute")
public class PmsProductAttribute implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "product_attribute_category_id")
    private Long productAttributeCategoryId;

    private String name;

    @Column(name = "select_type")
    @Schema(title = "Select type")
    private Integer selectType;

    @Column(name = "input_type")
    @Schema(title = "Input type")
    private Integer inputType;

    @Column(name = "input_list")
    @Schema(title = "Input list")
    private String inputList;

    @Schema(title = "Sort")
    private Integer sort;

    @Column(name = "filter_type")
    @Schema(title = "Filter type")
    private Integer filterType;

    @Column(name = "search_type")
    @Schema(title = "Search type")
    private Integer searchType;

    @Column(name = "related_status")
    @Schema(title = "Related status")
    private Integer relatedStatus;

    @Column(name = "hand_add_status")
    @Schema(title = "Hand add status")
    private Integer handAddStatus;

    @Schema(title = "Type")
    private Integer type;
}
