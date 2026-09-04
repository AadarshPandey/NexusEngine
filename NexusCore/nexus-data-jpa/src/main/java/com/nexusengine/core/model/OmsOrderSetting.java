package com.nexusengine.core.model;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import java.io.Serializable;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "oms_order_setting")
public class OmsOrderSetting implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "flash_order_overtime")
    @Schema(title = "Flash order overtime")
    private Integer flashOrderOvertime;

    @Column(name = "normal_order_overtime")
    @Schema(title = "Normal order overtime")
    private Integer normalOrderOvertime;

    @Column(name = "confirm_overtime")
    @Schema(title = "Confirm overtime")
    private Integer confirmOvertime;

    @Column(name = "finish_overtime")
    @Schema(title = "Finish overtime")
    private Integer finishOvertime;

    @Column(name = "comment_overtime")
    @Schema(title = "Comment overtime")
    private Integer commentOvertime;
}
