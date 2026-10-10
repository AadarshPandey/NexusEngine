package com.nexusengine.core.domain.iam.entity;

import com.nexusengine.core.domain.shared.converter.IntegerToBooleanConverter;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import java.io.Serializable;
import java.time.OffsetDateTime;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "console_menu", schema = "iam")
public class ConsoleMenu implements Serializable {

    private static final long serialVersionUID = 1L;

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
    @org.hibernate.annotations.JdbcTypeCode(org.hibernate.type.SqlTypes.SMALLINT)
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
    @jakarta.persistence.Convert(converter = com.nexusengine.core.domain.shared.converter.IntegerToBooleanConverter.class)
private Integer hidden;
}
