package com.nexusengine.core.model;

import jakarta.persistence.*;
import java.io.Serializable;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "category_attribute", schema = "catalog")
public class PmsProductCategoryAttributeRelation implements Serializable {

    private static final long serialVersionUID = 1L;

        @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    
private Long id;

        @Column(name = "category_id")
    
private Long productCategoryId;
        @com.fasterxml.jackson.annotation.JsonIgnore
    @jakarta.persistence.ManyToOne(fetch = jakarta.persistence.FetchType.LAZY)
    @jakarta.persistence.JoinColumn(name = "category_id", insertable = false, updatable = false)
    
private PmsProductCategory productCategory;

        @Column(name = "attribute_id")
    
private Long productAttributeId;
        @com.fasterxml.jackson.annotation.JsonIgnore
    @jakarta.persistence.ManyToOne(fetch = jakarta.persistence.FetchType.LAZY)
    @jakarta.persistence.JoinColumn(name = "attribute_id", insertable = false, updatable = false)
    
private PmsProductAttribute productAttribute;
}
