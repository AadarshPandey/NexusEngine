package com.nexusengine.core.model;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "ums_admin")
public class UmsAdmin implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String username;

    @com.fasterxml.jackson.annotation.JsonIgnore
    private String password;

    @Schema(title = "Icon")
    private String icon;

    @Schema(title = "Email")
    private String email;

    @Column(name = "nick_name")
    @Schema(title = "Nick name")
    private String nickName;

    @Schema(title = "Note")
    private String note;

    @Column(name = "create_time")
    @Schema(title = "Create time")
    private LocalDateTime createTime;

    @Column(name = "login_time")
    @Schema(title = "Login time")
    private LocalDateTime loginTime;

    @Schema(title = "Status")
    private Integer status;

    @Column(name = "vendor_id")
    @Schema(title = "Vendor ID")
    private Long vendorId;
}
