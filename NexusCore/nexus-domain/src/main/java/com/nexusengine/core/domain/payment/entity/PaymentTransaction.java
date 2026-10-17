package com.nexusengine.core.domain.payment.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import com.nexusengine.core.domain.ordering.entity.CustomerOrder;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.time.OffsetDateTime;
import java.math.BigDecimal;

@Getter
@Setter
@Entity
@Table(name = "payment_transaction", schema = "payment")
public class PaymentTransaction {
        @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)

private Long id;

        @Column(name = "order_id", nullable = false)

private Long orderId;
        @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id", insertable = false, updatable = false)

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
        @JdbcTypeCode(SqlTypes.JSON)
        private String webhookPayload;

        @Column(name = "created_at")
        private OffsetDateTime createTime;

    public String getProviderTxnId() {
        return transactionId;
    }

    public void setProviderTxnId(String providerTxnId) {
        this.transactionId = providerTxnId;
    }

    public String getTxnType() {
        return transactionType;
    }

    public void setTxnType(String txnType) {
        this.transactionType = txnType;
    }

    public OffsetDateTime getCreatedAt() {
        return createTime;
    }

    public void setCreatedAt(OffsetDateTime createdAt) {
        this.createTime = createdAt;
    }

    @PrePersist
    protected void onCreate() {
        if (createTime == null) {
            createTime = OffsetDateTime.now();
        }
    }
}
