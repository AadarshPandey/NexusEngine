package com.nexusengine.core.model;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import java.io.Serializable;
import java.util.Date;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "ums_menu")
public class UmsMenu implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "parent_id")
    @Schema(title = "Parent id")
    private Long parentId;

    @Column(name = "create_time")
    @Schema(title = "Create time")
    private Date createTime;

    @Schema(title = "Title")
    private String title;

    @Schema(title = "Level")
    private Integer level;

    @Schema(title = "Sort")
    private Integer sort;

    @Schema(title = "Name")
    private String name;

    @Schema(title = "Icon")
    private String icon;

    @Schema(title = "Hidden")
    private Integer hidden;
}
