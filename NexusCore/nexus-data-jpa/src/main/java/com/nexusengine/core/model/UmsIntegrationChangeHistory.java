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
@Table(name = "ums_integration_change_history")
public class UmsIntegrationChangeHistory implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "member_id")
    private Long memberId;

    @Column(name = "create_time")
    private Date createTime;

    @Column(name = "change_type")
    @Schema(title = "Change type")
    private Integer changeType;

    @Column(name = "change_count")
    @Schema(title = "Change count")
    private Integer changeCount;

    @Column(name = "operate_man")
    @Schema(title = "Operate man")
    private String operateMan;

    @Column(name = "operate_note")
    @Schema(title = "Operate note")
    private String operateNote;

    @Column(name = "source_type")
    @Schema(title = "Source type")
    private Integer sourceType;
}
