package com.nexusengine.core.model;

import jakarta.persistence.*;
import lombok.Data;
import java.util.Date;

@Data
@Entity
@Table(name = "outbox_event")
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

    @Temporal(TemporalType.TIMESTAMP)
    private Date createdAt;
}
