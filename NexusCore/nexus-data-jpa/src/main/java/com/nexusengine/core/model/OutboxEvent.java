package com.nexusengine.core.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name = "outbox_event", schema = "integration")
public class OutboxEvent {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String aggregateType;

    @Column(nullable = false)
    private String aggregateId;

    @Column(nullable = false)
    private String type;

    @Column(columnDefinition = "TEXT")
    private String payload;

    @Column(nullable = false)
    private String status; // PENDING, SENT, FAILED

    @Column(columnDefinition = "INTEGER DEFAULT 0")
    private Integer retryCount = 0;

    private LocalDateTime createdAt;
}
