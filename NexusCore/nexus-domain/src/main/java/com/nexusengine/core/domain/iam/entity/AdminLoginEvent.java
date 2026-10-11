package com.nexusengine.core.domain.iam.entity;

import jakarta.persistence.*;
import java.io.Serializable;
import java.time.OffsetDateTime;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "admin_login_event", schema = "iam")
public class AdminLoginEvent implements Serializable {

    private static final long serialVersionUID = 1L;

        @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)

private Long id;

        @Column(name = "admin_user_id")

private Long adminId;
        @com.fasterxml.jackson.annotation.JsonIgnore
    @jakarta.persistence.ManyToOne(fetch = jakarta.persistence.FetchType.LAZY)
    @jakarta.persistence.JoinColumn(name = "admin_user_id", insertable = false, updatable = false)

private AdminUser admin;

        @Column(name = "occurred_at")

private OffsetDateTime createTime;

            @Column(name = "ip_address")
private String ip;


}
