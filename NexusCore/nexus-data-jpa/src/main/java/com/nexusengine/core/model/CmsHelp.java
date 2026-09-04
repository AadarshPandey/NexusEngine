package com.nexusengine.core.model;

import jakarta.persistence.*;
import java.io.Serializable;
import java.util.Date;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "cms_help")
public class CmsHelp implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "category_id")
    private Long categoryId;

    private String icon;

    private String title;

    @Column(name = "show_status")
    private Integer showStatus;

    @Column(name = "create_time")
    private Date createTime;

    @Column(name = "read_count")
    private Integer readCount;

    private String content;
}
