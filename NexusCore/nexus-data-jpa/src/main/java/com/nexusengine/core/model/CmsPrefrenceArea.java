package com.nexusengine.core.model;

import jakarta.persistence.*;
import java.io.Serializable;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "cms_prefrence_area")
public class CmsPrefrenceArea implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @Column(name = "sub_title")
    private String subTitle;

    private Integer sort;

    @Column(name = "show_status")
    private Integer showStatus;
}
