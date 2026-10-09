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
@Table(name = "member_login_event", schema = "customer")
public class UmsMemberLoginLog implements Serializable {

    private static final long serialVersionUID = 1L;

        @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    
private Long id;

        @Column(name = "member_id")
    
private Long memberId;
        @com.fasterxml.jackson.annotation.JsonIgnore
    @jakarta.persistence.ManyToOne(fetch = jakarta.persistence.FetchType.LAZY)
    @jakarta.persistence.JoinColumn(name = "member_id", insertable = false, updatable = false)
    
private UmsMember member;

        @Column(name = "occurred_at")
    
private OffsetDateTime createTime;

            @Column(name = "ip_address")
private String ip;

    private String city;

        @Column(name = "login_method")
    @Schema(title = "Login type")
    @org.hibernate.annotations.JdbcTypeCode(org.hibernate.type.SqlTypes.SMALLINT)
private Integer loginType;

            @Column(name = "region")
private String province;
}
