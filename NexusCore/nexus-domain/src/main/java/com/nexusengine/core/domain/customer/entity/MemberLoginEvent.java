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
@Table(name = "member_login_event", schema = "customer")
public class MemberLoginEvent {
        @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)

private Long id;

        @Column(name = "member_id")

private Long memberId;
        @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", insertable = false, updatable = false)

private Member member;

        @Column(name = "occurred_at")

private OffsetDateTime createTime;

            @Column(name = "ip_address")
private String ip;

    private String city;

        @Column(name = "login_method")
    @Schema(title = "Login type")
    @JdbcTypeCode(SqlTypes.SMALLINT)
private Integer loginType;

            @Column(name = "region")
private String province;
}
