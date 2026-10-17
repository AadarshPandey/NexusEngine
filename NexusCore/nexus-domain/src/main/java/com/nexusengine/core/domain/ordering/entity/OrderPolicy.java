package com.nexusengine.core.domain.ordering.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "order_policy", schema = "ordering")
public class OrderPolicy {
        @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)

private Long id;

        @Column(name = "payment_timeout_min")
    @Schema(title = "Normal order overtime")

private Integer paymentTimeoutMin;

        @Column(name = "auto_confirm_receipt_days")
    @Schema(title = "Confirm overtime")

private Integer autoConfirmReceiptDays;

        @Column(name = "auto_complete_days")
    @Schema(title = "Finish overtime")

private Integer autoCompleteDays;

        @Column(name = "review_window_days")
    @Schema(title = "Comment overtime")

private Integer reviewWindowDays;
}
