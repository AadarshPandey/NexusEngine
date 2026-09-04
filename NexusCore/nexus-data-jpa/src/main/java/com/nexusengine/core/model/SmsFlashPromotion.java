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
@Table(name = "sms_flash_promotion")
public class SmsFlashPromotion implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Schema(title = "Title")
    private String title;

    @Column(name = "start_date")
    @Schema(title = "Start date")
    private Date startDate;

    @Column(name = "end_date")
    @Schema(title = "End date")
    private Date endDate;

    @Schema(title = "Status")
    private Integer status;

    @Column(name = "create_time")
    @Schema(title = "Create time")
    private Date createTime;
}
