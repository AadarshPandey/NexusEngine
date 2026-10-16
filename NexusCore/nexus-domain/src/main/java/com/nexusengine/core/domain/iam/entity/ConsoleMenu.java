package com.nexusengine.core.domain.iam.entity;

import org.hibernate.type.SqlTypes;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.OffsetDateTime;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

@Getter
@Setter
@Entity
@Table(name = "console_menu", schema = "iam")
public class ConsoleMenu {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "parent_id")
    @Schema(title = "Parent id")
    private Long parentId;

    @Column(name = "created_at")
    @Schema(title = "Create time")
    private OffsetDateTime createTime;

    @Schema(title = "Title")
    private String title;

    @Schema(title = "Level")
    @Column(name = "depth")
    @JdbcTypeCode(SqlTypes.SMALLINT)
    private Integer level;

    @Schema(title = "Sort")
    @Column(name = "display_order")
    private Integer sort;

    @Schema(title = "Name")
    @Column(name = "route_name")
    private String name;

    @Schema(title = "Icon")
    private String icon;

    @Schema(title = "Hidden")
    @Column(name = "is_hidden")
    private Boolean hidden;
}
