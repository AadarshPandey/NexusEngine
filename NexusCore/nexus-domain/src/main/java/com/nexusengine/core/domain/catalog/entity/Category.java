package com.nexusengine.core.domain.catalog.entity;


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

    @Column(name = "product_count")
    @com.fasterxml.jackson.annotation.JsonProperty("quantity")
    private Integer productCount;

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
    @com.fasterxml.jackson.annotation.JsonProperty("iconUrl")
    @Schema(title = "Icon")
    @Column(name = "icon_url")
    private String icon;
}
