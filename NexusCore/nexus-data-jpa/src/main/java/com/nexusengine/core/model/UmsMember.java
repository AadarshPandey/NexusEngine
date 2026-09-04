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
@Table(name = "ums_member")
public class UmsMember implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "member_level_id")
    private Long memberLevelId;

    @Schema(title = "Username")
    private String username;

    @Schema(title = "Password")
    private String password;

    @Schema(title = "Nickname")
    private String nickname;

    @Schema(title = "Phone")
    private String phone;

    @Schema(title = "Status")
    private Integer status;

    @Column(name = "create_time")
    @Schema(title = "Create time")
    private Date createTime;

    @Schema(title = "Icon")
    private String icon;

    @Schema(title = "Gender")
    private Integer gender;

    @Schema(title = "Birthday")
    private Date birthday;

    @Schema(title = "City")
    private String city;

    @Schema(title = "Job")
    private String job;

    @Column(name = "personalized_signature")
    @Schema(title = "Personalized signature")
    private String personalizedSignature;

    @Column(name = "source_type")
    @Schema(title = "Source type")
    private Integer sourceType;

    @Schema(title = "Integration")
    private Integer integration;

    @Schema(title = "Growth")
    private Integer growth;

    @Column(name = "luckey_count")
    @Schema(title = "Luckey count")
    private Integer luckeyCount;

    @Column(name = "history_integration")
    @Schema(title = "History integration")
    private Integer historyIntegration;
}
