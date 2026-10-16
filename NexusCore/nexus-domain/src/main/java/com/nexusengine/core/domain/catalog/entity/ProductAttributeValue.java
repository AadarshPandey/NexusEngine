package com.nexusengine.core.domain.catalog.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "product_attribute_value", schema = "catalog")
public class ProductAttributeValue {
        @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)

private Long id;

        @Column(name = "product_id")

private Long productId;
        @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", insertable = false, updatable = false)

private Product product;

        @Column(name = "attribute_id")

private Long productAttributeId;
        @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "attribute_id", insertable = false, updatable = false)

private AttributeDefinition productAttribute;

        @Schema(title = "Value")

private String value;
}
