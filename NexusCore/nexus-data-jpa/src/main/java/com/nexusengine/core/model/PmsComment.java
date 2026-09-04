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
@Table(name = "pms_comment")
public class PmsComment implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "product_id")
    private Long productId;

    @Column(name = "member_nick_name")
    private String memberNickName;

    @Column(name = "product_name")
    private String productName;

    @Schema(title = "Star")
    private Integer star;

    @Column(name = "member_ip")
    @Schema(title = "Member ip")
    private String memberIp;

    @Column(name = "create_time")
    private Date createTime;

    @Column(name = "show_status")
    private Integer showStatus;

    @Column(name = "product_attribute")
    @Schema(title = "Product attribute")
    private String productAttribute;

    @Column(name = "collect_couont")
    private Integer collectCouont;

    @Column(name = "read_count")
    private Integer readCount;

    @Schema(title = "Pics")
    private String pics;

    @Column(name = "member_icon")
    @Schema(title = "Member icon")
    private String memberIcon;

    @Column(name = "replay_count")
    private Integer replayCount;

    private String content;
}
