package com.nexusengine.core.model;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import java.io.Serializable;
import java.time.OffsetDateTime;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "admin_user", schema = "iam")
public class UmsAdmin implements Serializable {

    private static final long serialVersionUID = 1L;

        @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    
private Long id;

    private String username;

            @com.fasterxml.jackson.annotation.JsonIgnore
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
    @jakarta.persistence.Convert(converter = com.nexusengine.core.model.converter.IntegerToBooleanConverter.class)
private Integer status;

        @Column(name = "vendor_id")
    @Schema(title = "Vendor ID")
    
private Long vendorId;
}
