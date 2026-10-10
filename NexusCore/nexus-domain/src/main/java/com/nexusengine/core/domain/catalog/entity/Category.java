package com.nexusengine.core.domain.catalog.entity;

import com.nexusengine.core.domain.shared.converter.IntegerToBooleanConverter;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import java.io.Serializable;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "category", schema = "catalog")
public class Category implements Serializable {

    private static final long serialVersionUID = 1L;

        @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    
private Long id;

        @Column(name = "parent_id")
    @Schema(title = "Parent id")
    
private Long parentId;

    private String name;

            @Schema(title = "Level")
    @com.fasterxml.jackson.annotation.JsonProperty("depth")
    @Column(name = "depth")
    @org.hibernate.annotations.JdbcTypeCode(org.hibernate.type.SqlTypes.SMALLINT)
private Integer level;

            @org.hibernate.annotations.Formula("(SELECT COUNT(*) FROM catalog.product p WHERE (p.category_id = id OR p.category_id IN (SELECT c.id FROM catalog.category c WHERE c.parent_id = id)) AND p.deleted_at IS NULL)")
    @com.fasterxml.jackson.annotation.JsonProperty("quantity")
    private Integer productCount;

        @com.fasterxml.jackson.annotation.JsonProperty("unitOfMeasure")
        @Column(name = "unit_of_measure")
    
private String productUnit;

        @com.fasterxml.jackson.annotation.JsonProperty("isInNavigation")
        @Column(name = "is_in_navigation")
    @Schema(title = "Nav status")
    @jakarta.persistence.Convert(converter = com.nexusengine.core.domain.shared.converter.IntegerToBooleanConverter.class)
private Integer navStatus;

        @com.fasterxml.jackson.annotation.JsonProperty("isVisible")
        @Column(name = "is_visible")
    @Schema(title = "Show status")
    @jakarta.persistence.Convert(converter = com.nexusengine.core.domain.shared.converter.IntegerToBooleanConverter.class)
private Integer showStatus;

            @com.fasterxml.jackson.annotation.JsonProperty("displayOrder")
            @Column(name = "display_order")
private Integer sort;

            @com.fasterxml.jackson.annotation.JsonProperty("iconUrl")
            @Schema(title = "Icon")
    @Column(name = "icon_url")
private String icon;

            @com.fasterxml.jackson.annotation.JsonProperty("searchKeywords")
            @Column(name = "search_keywords")
private String keywords;

        @Schema(title = "Description")
    
private String description;
}
