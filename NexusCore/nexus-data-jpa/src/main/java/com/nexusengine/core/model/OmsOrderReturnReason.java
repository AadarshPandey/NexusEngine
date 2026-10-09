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
@Table(name = "return_reason", schema = "ordering")
public class OmsOrderReturnReason implements Serializable {

    private static final long serialVersionUID = 1L;

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
    @jakarta.persistence.Convert(converter = com.nexusengine.core.model.converter.IntegerToBooleanConverter.class)
private Integer status;

        @Column(name = "created_at")
    @Schema(title = "Create time")
private OffsetDateTime createdAt;
}
