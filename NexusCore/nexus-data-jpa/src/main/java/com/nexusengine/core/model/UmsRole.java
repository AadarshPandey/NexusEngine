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
@Table(name = "ums_role")
public class UmsRole implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Schema(title = "Name")
    private String name;

    @Schema(title = "Description")
    private String description;

    @Column(name = "admin_count")
    @Schema(title = "Admin count")
    private Integer adminCount;

    @Column(name = "create_time")
    @Schema(title = "Create time")
    private Date createTime;

    @Schema(title = "Status")
    private Integer status;

    private Integer sort;
}
