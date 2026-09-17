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

    @Schema(title = "Username")
    private String username;

    @Schema(title = "Password")
    @com.fasterxml.jackson.annotation.JsonIgnore
    private String password;

    @Schema(title = "Nickname")
    private String nickname;

    @Schema(title = "Phone")
    private String phone;

    @Schema(title = "Email")
    private String email;

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



    @Column(name = "personalized_signature")
    @Schema(title = "Personalized signature")
    private String personalizedSignature;

    @Column(name = "source_type")
    @Schema(title = "Source type")
    private Integer sourceType;
    private Integer points;

    @Column(name = "bonus_draws_remaining")
    @Schema(title = "Luckey count")
    private Integer bonusDrawsRemaining;

    @Column(name = "lifetime_points")
    @Schema(title = "History integration")
    private Integer lifetimePoints;
}
