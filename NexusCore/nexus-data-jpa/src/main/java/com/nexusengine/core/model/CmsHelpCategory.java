package com.nexusengine.core.model;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import java.io.Serializable;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "cms_help_category")
public class CmsHelpCategory implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @Schema(title = "Icon")
    private String icon;

    @Column(name = "help_count")
    @Schema(title = "Help count")
    private Integer helpCount;

    @Column(name = "show_status")
    private Integer showStatus;

    private Integer sort;
}
