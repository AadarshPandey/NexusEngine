package com.nexusengine.core.domain.catalog.entity;

import org.hibernate.annotations.Formula;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "product_type", schema = "catalog")
public class ProductType {
        @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)

private Long id;

    private String name;

            @Formula("(SELECT COUNT(*) FROM catalog.attribute_definition a WHERE a.product_type_id = id AND a.kind = 0)")
    private Integer attributeCount;

            @Formula("(SELECT COUNT(*) FROM catalog.attribute_definition a WHERE a.product_type_id = id AND a.kind = 1)")
    private Integer paramCount;
}
