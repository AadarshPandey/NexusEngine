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
@Table(name = "ums_permission")
public class UmsPermission implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Schema(title = "Pid")
    private Long pid;

    @Schema(title = "Name")
    private String name;

    @Schema(title = "Value")
    private String value;

    @Schema(title = "Icon")
    private String icon;

    @Schema(title = "Type")
    private Integer type;

    @Schema(title = "Uri")
    private String uri;

    @Schema(title = "Status")
    private Integer status;

    @Column(name = "create_time")
    @Schema(title = "Create time")
    private Date createTime;

    @Schema(title = "Sort")
    private Integer sort;
}
