package com.nexusengine.core.domain.catalog.entity;

import jakarta.persistence.FetchType;
import org.hibernate.type.SqlTypes;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

@Getter
@Setter
@Entity
@Table(name = "attribute_definition", schema = "catalog")
public class AttributeDefinition {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "product_type_id")
    private Long productAttributeCategoryId;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_type_id", insertable = false, updatable = false)
    private ProductType productAttributeCategory;

    private String name;

    @Column(name = "selection_mode")
    @Schema(title = "Selection mode")
    @JdbcTypeCode(SqlTypes.SMALLINT)
    private Integer selectionMode;

    @Column(name = "input_mode")
    @Schema(title = "Input mode")
    @JdbcTypeCode(SqlTypes.SMALLINT)
    private Integer inputMode;

    @Column(name = "allowed_values")
    @Schema(title = "Allowed values")
    private String allowedValues;

    @Column(name = "kind")
    @JdbcTypeCode(SqlTypes.SMALLINT)
    private Integer type;
}
