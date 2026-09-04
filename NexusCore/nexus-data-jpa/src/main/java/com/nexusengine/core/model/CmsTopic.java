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
@Table(name = "cms_topic")
public class CmsTopic implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "category_id")
    private Long categoryId;

    private String name;

    @Column(name = "create_time")
    private Date createTime;

    @Column(name = "start_time")
    private Date startTime;

    @Column(name = "end_time")
    private Date endTime;

    @Column(name = "attend_count")
    @Schema(title = "Attend count")
    private Integer attendCount;

    @Column(name = "attention_count")
    @Schema(title = "Attention count")
    private Integer attentionCount;

    @Column(name = "read_count")
    private Integer readCount;

    @Column(name = "award_name")
    @Schema(title = "Award name")
    private String awardName;

    @Column(name = "attend_type")
    @Schema(title = "Attend type")
    private String attendType;

    @Schema(title = "Content")
    private String content;
}
