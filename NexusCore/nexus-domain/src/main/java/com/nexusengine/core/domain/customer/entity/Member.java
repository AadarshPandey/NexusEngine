package com.nexusengine.core.domain.customer.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import java.io.Serializable;
import java.time.OffsetDateTime;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "member", schema = "customer")
public class Member implements Serializable {

    private static final long serialVersionUID = 1L;

        @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)

private Long id;

        @Schema(title = "Username")

private String username;

            @Schema(title = "Password")
    @com.fasterxml.jackson.annotation.JsonIgnore
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
    @Column(name = "account_status")
    @org.hibernate.annotations.JdbcTypeCode(org.hibernate.type.SqlTypes.SMALLINT)
private Integer status;

        @Column(name = "created_at")
    @Schema(title = "Create time")

private OffsetDateTime createdAt;

            @Schema(title = "Icon")
    @Column(name = "avatar_url")
private String iconUrl;

        @Schema(title = "Gender")
    @org.hibernate.annotations.JdbcTypeCode(org.hibernate.type.SqlTypes.SMALLINT)
private Integer gender;





        @Column(name = "signup_channel")
    @Schema(title = "Source type")
    @org.hibernate.annotations.JdbcTypeCode(org.hibernate.type.SqlTypes.SMALLINT)
    private Integer signupChannel;
}
