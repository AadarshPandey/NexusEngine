package com.nexusengine.core.domain.catalog.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import java.io.Serializable;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "product_type", schema = "catalog")
public class ProductType implements Serializable {

    private static final long serialVersionUID = 1L;

        @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    
private Long id;

    private String name;

            @org.hibernate.annotations.Formula("(SELECT COUNT(*) FROM catalog.attribute_definition a WHERE a.product_type_id = id AND a.kind = 0)")
    private Integer attributeCount;

            @org.hibernate.annotations.Formula("(SELECT COUNT(*) FROM catalog.attribute_definition a WHERE a.product_type_id = id AND a.kind = 1)")
    private Integer paramCount;
}
