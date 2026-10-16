package com.nexusengine.core.domain.messaging.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.time.OffsetDateTime;

@Getter
@Setter
@Entity
@Table(name = "outbox_event", schema = "messaging")
public class OutboxEvent {
        @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)

private Long id;

        @Column(nullable = false)

private String aggregateType;

        @Column(nullable = false)

private String aggregateId;

    @Column(name = "event_type", nullable = false)
private String type;

        @Column(columnDefinition = "TEXT")

private String payload;

        @Column(nullable = false)

private String status; // PENDING, SENT, FAILED



    @Column(name = "attempt_count", columnDefinition = "INTEGER DEFAULT 0")
    private Integer retryCount = 0;

    public Integer getAttemptCount() {
        return retryCount;
    }

    public void setAttemptCount(Integer attemptCount) {
        this.retryCount = attemptCount;
    }

    private OffsetDateTime createdAt;
}
