package com.nexusengine.core.domain.ordering.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.OffsetDateTime;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "return_reason", schema = "ordering")
public class ReturnReason {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Schema(title = "Name")
    @Column(name = "label")
    private String name;

    @Column(name = "display_order")
    private Integer displayOrder;

    @Schema(title = "Status")
    @Column(name = "is_active")
    private Boolean status;

    @Column(name = "created_at")
    @Schema(title = "Create time")
    private OffsetDateTime createdAt;
}
