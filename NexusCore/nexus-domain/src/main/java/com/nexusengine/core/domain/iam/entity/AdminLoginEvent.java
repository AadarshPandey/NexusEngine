package com.nexusengine.core.domain.iam.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.*;
import java.time.OffsetDateTime;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "admin_login_event", schema = "iam")
public class AdminLoginEvent {
        @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)

private Long id;

        @Column(name = "admin_user_id")

private Long adminId;
        @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "admin_user_id", insertable = false, updatable = false)

private AdminUser admin;

        @Column(name = "occurred_at")

private OffsetDateTime createTime;

            @Column(name = "ip_address")
private String ip;

    public OffsetDateTime getOccurredAt() {
        return createTime;
    }

    public void setOccurredAt(OffsetDateTime occurredAt) {
        this.createTime = occurredAt;
    }

    public String getIpAddress() {
        return ip;
    }

    public void setIpAddress(String ipAddress) {
        this.ip = ipAddress;
    }
}
