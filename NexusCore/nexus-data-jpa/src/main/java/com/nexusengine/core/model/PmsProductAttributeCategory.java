package com.nexusengine.core.model;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import java.io.Serializable;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "pms_product_attribute_category")
public class PmsProductAttributeCategory implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @Column(name = "attribute_count")
    @Schema(title = "Attribute count")
    private Integer attributeCount;

    @Column(name = "param_count")
    @Schema(title = "Param count")
    private Integer paramCount;
}
