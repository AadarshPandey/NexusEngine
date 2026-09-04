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
@Table(name = "sms_flash_promotion_session")
public class SmsFlashPromotionSession implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(title = "Id")
    private Long id;

    @Schema(title = "Name")
    private String name;

    @Column(name = "start_time")
    @Schema(title = "Start time")
    private Date startTime;

    @Column(name = "end_time")
    @Schema(title = "End time")
    private Date endTime;

    @Schema(title = "Status")
    private Integer status;

    @Column(name = "create_time")
    @Schema(title = "Create time")
    private Date createTime;
}
