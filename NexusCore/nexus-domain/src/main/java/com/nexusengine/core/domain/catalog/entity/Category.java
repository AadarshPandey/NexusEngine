package com.nexusengine.core.domain.catalog.entity;

import org.hibernate.type.SqlTypes;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

@Getter
@Setter
@Entity
@Table(name = "category", schema = "catalog")
public class Category {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "parent_id")
    @Schema(title = "Parent id")
    private Long parentId;

    private String name;

    @Schema(title = "Depth")
    @Column(name = "depth")
    @JdbcTypeCode(SqlTypes.SMALLINT)
    private Integer depth;

    @Column(name = "product_count")
    private Integer productCount;

    @Column(name = "is_in_navigation")
    @Schema(title = "Navigation status")
    private Boolean isInNavigation;

    @Column(name = "is_visible")
    @Schema(title = "Visibility status")
    private Boolean isVisible;

    @Schema(title = "Icon URL")
    @Column(name = "icon_url")
    private String iconUrl;
}
