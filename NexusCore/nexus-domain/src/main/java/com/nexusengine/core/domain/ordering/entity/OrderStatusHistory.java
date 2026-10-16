package com.nexusengine.core.domain.ordering.entity;

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
@Table(name = "order_status_history", schema = "ordering")
public class OrderStatusHistory {
        @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)

private Long id;

        @Column(name = "order_id")
    @Schema(title = "Order id")

private Long orderId;
        @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id", insertable = false, updatable = false)

private CustomerOrder order;

        @Column(name = "actor_ref")
    @Schema(title = "Operate man")

private String operateMan;

        @Column(name = "created_at")
    @Schema(title = "Create time")

private OffsetDateTime createTime;

        @Column(name = "to_status")
    @Schema(title = "Order status")
    @JdbcTypeCode(SqlTypes.SMALLINT)
private Integer orderStatus;

        @Schema(title = "Note")

private String note;
}
