package com.nexusengine.core.domain.iam.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.OffsetDateTime;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "admin_user", schema = "iam")
public class AdminUser {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String username;

    @JsonIgnore
    @Column(name = "password_hash")
    private String password;

    @Schema(title = "Icon")
    @Column(name = "avatar_url")
    private String icon;

    @Schema(title = "Email")
    private String email;

    @Column(name = "nick_name")
    @Schema(title = "Nick name")
    private String nickName;

    @Schema(title = "Note")
    private String note;

    @Column(name = "created_at")
    @Schema(title = "Create time")
    private OffsetDateTime createTime;

    @Column(name = "last_login_at")
    @Schema(title = "Login time")
    private OffsetDateTime loginTime;

    @Schema(title = "Status")
    @Column(name = "is_enabled")
    private Boolean status;

    @Schema(title = "Brand ID (scoped vendor admin)")
    @Column(name = "brand_id")
    private Long brandId;
}
