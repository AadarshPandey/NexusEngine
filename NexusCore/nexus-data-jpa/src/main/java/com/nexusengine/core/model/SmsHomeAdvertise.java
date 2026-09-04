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
@Table(name = "sms_home_advertise")
public class SmsHomeAdvertise implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @Schema(title = "Type")
    private Integer type;

    private String pic;

    @Column(name = "start_time")
    private Date startTime;

    @Column(name = "end_time")
    private Date endTime;

    @Schema(title = "Status")
    private Integer status;

    @Column(name = "click_count")
    @Schema(title = "Click count")
    private Integer clickCount;

    @Column(name = "order_count")
    @Schema(title = "Order count")
    private Integer orderCount;

    @Schema(title = "Url")
    private String url;

    @Schema(title = "Note")
    private String note;

    @Schema(title = "Sort")
    private Integer sort;
}
