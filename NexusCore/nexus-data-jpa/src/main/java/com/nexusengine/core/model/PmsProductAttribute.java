package com.nexusengine.core.model;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import java.io.Serializable;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "attribute_definition", schema = "catalog")
public class PmsProductAttribute implements Serializable {

    private static final long serialVersionUID = 1L;

        @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    
private Long id;

        @Column(name = "product_type_id")
    
private Long productAttributeCategoryId;
        @com.fasterxml.jackson.annotation.JsonIgnore
    @jakarta.persistence.ManyToOne(fetch = jakarta.persistence.FetchType.LAZY)
    @jakarta.persistence.JoinColumn(name = "product_type_id", insertable = false, updatable = false)
    
private PmsProductAttributeCategory productAttributeCategory;

    private String name;

        @com.fasterxml.jackson.annotation.JsonProperty("selectionMode")
        @Column(name = "selection_mode")
    @Schema(title = "Select type")
    @org.hibernate.annotations.JdbcTypeCode(org.hibernate.type.SqlTypes.SMALLINT)
private Integer selectType;

        @com.fasterxml.jackson.annotation.JsonProperty("inputMode")
        @Column(name = "input_mode")
    @Schema(title = "Input type")
    @org.hibernate.annotations.JdbcTypeCode(org.hibernate.type.SqlTypes.SMALLINT)
private Integer inputType;

        @com.fasterxml.jackson.annotation.JsonProperty("allowedValues")
        @Column(name = "allowed_values")
    @Schema(title = "Input list")
    
private String inputList;

    @Column(name = "kind")
    @org.hibernate.annotations.JdbcTypeCode(org.hibernate.type.SqlTypes.SMALLINT)
private Integer type;
}
