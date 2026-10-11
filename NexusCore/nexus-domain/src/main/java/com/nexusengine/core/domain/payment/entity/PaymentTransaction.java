package com.nexusengine.core.domain.payment.entity;

import com.nexusengine.core.domain.ordering.entity.CustomerOrder;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.io.Serializable;
import java.time.OffsetDateTime;
import java.math.BigDecimal;

@Getter
@Setter
@Entity
@Table(name = "payment_transaction", schema = "payment")
public class PaymentTransaction implements Serializable {

    private static final long serialVersionUID = 1L;

        @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)

private Long id;

        @Column(name = "order_id", nullable = false)

private Long orderId;
        @com.fasterxml.jackson.annotation.JsonIgnore
    @jakarta.persistence.ManyToOne(fetch = jakarta.persistence.FetchType.LAZY)
    @jakarta.persistence.JoinColumn(name = "order_id", insertable = false, updatable = false)

private CustomerOrder order;

        @Column(name = "provider_txn_id", nullable = false, unique = true)

private String transactionId;

        @Column(name = "txn_type", nullable = false, length = 50)

private String transactionType;

        @Column(name = "amount", nullable = false, precision = 38, scale = 2)

private BigDecimal amount;

        @Column(name = "status", nullable = false, length = 50)

private String status;

        @Column(name = "provider_payload", columnDefinition = "jsonb")

private String webhookPayload;

        @Column(name = "created_at")

private OffsetDateTime createTime;
}
