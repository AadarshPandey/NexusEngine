package com.nexusengine.core.domain.catalog.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "category_attribute", schema = "catalog")
public class CategoryAttribute {
        @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)

private Long id;

        @Column(name = "category_id")

private Long productCategoryId;
        @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id", insertable = false, updatable = false)

private Category productCategory;

        @Column(name = "attribute_id")

private Long productAttributeId;
        @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "attribute_id", insertable = false, updatable = false)

private AttributeDefinition productAttribute;
}
