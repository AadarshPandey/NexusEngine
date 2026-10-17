package com.nexusengine.core.domain.customer.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import java.time.OffsetDateTime;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "member", schema = "customer")
public class Member {
        @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)

private Long id;

        @Schema(title = "Username")

private String username;

            @Schema(title = "Password")
    @JsonIgnore
    @Column(name = "password_hash")
private String password;

            @Schema(title = "Nickname")
    @Column(name = "display_name")
private String displayName;

            @Schema(title = "Phone")
    @Column(name = "phone_e164")
private String contactPhone;

        @Schema(title = "Email")

private String email;

    @Schema(title = "Status")
    @Column(name = "account_status", nullable = false)
    @JdbcTypeCode(SqlTypes.SMALLINT)
    private Integer status = 1;

    @Column(name = "created_at", nullable = false)
    @Schema(title = "Create time")
    private OffsetDateTime createdAt;

    @Schema(title = "Icon")
    @Column(name = "avatar_url")
    private String iconUrl;

    @Schema(title = "Gender")
    @Column(name = "gender", nullable = false)
    @JdbcTypeCode(SqlTypes.SMALLINT)
    private Integer gender = 0;

    @Column(name = "signup_channel", nullable = false)
    @Schema(title = "Source type")
    @JdbcTypeCode(SqlTypes.SMALLINT)
    private Integer signupChannel = 0;

    public String getAvatarUrl() {
        return iconUrl;
    }

    public void setAvatarUrl(String avatarUrl) {
        this.iconUrl = avatarUrl;
    }

    public String getPhoneE164() {
        return contactPhone;
    }

    public void setPhoneE164(String phoneE164) {
        this.contactPhone = phoneE164;
    }

    public Integer getAccountStatus() {
        return status;
    }

    public void setAccountStatus(Integer accountStatus) {
        this.status = accountStatus;
    }

    @PrePersist
    protected void onCreate() {
        if (createdAt == null) {
            createdAt = OffsetDateTime.now();
        }
        if (gender == null) {
            gender = 0;
        }
        if (signupChannel == null) {
            signupChannel = 0;
        }
        if (status == null) {
            status = 1;
        }
    }
}
