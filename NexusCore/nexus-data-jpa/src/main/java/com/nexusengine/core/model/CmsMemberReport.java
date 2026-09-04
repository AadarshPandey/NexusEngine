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
@Table(name = "cms_member_report")
public class CmsMemberReport implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "report_type")
    @Schema(title = "Report type")
    private Integer reportType;

    @Column(name = "report_member_name")
    @Schema(title = "Report member name")
    private String reportMemberName;

    @Column(name = "create_time")
    private Date createTime;

    @Column(name = "report_object")
    private String reportObject;

    @Column(name = "report_status")
    @Schema(title = "Report status")
    private Integer reportStatus;

    @Column(name = "handle_status")
    @Schema(title = "Handle status")
    private Integer handleStatus;

    private String note;
}
