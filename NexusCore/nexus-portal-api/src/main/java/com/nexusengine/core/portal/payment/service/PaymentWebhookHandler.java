package com.nexusengine.core.portal.payment.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.nexusengine.core.domain.ordering.entity.CustomerOrder;
import com.nexusengine.core.domain.ordering.entity.OrderLine;
import com.nexusengine.core.domain.ordering.repository.CustomerOrderRepository;
import com.nexusengine.core.domain.ordering.repository.OrderLineRepository;
import com.nexusengine.core.domain.payment.entity.PaymentTransaction;
import com.nexusengine.core.domain.payment.repository.PaymentTransactionRepository;
import com.nexusengine.core.portal.catalog.service.StockReservationService;
import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service handling secure, idempotent payment webhooks.
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class PaymentWebhookHandler {

    private final PaymentTransactionRepository paymentTransactionRepository;
    private final CustomerOrderRepository orderRepository;
    private final OrderLineRepository orderLineRepository;
    private final StockReservationService stockReservationService;
    private final PaymentGatewayService paymentGatewayService;
    private final ObjectMapper objectMapper;

    @Transactional
    public void handlePaymentWebhook(String payload, String signature) {
        if (signature != null && !signature.isBlank()) {
            boolean valid = paymentGatewayService.verifyWebhookSignature(payload, signature);
            if (!valid) {
                log.warn("Invalid payment webhook signature");
                throw new IllegalArgumentException("Invalid webhook signature");
            }
        }

        try {
            JsonNode root = objectMapper.readTree(payload);
            String event = root.path("event").asText();

            if (!"payment.captured".equals(event)) {
                log.info("Ignoring non-capture webhook event: {}", event);
                return;
            }

            JsonNode paymentEntity = root.path("payload").path("payment").path("entity");
            String transactionId = paymentEntity.path("id").asText();
            String orderIdStr = paymentEntity.path("notes").path("order_id").asText();

            Long orderId = null;
            if (orderIdStr != null && !orderIdStr.isBlank()) {
                try {
                    orderId = Long.parseLong(orderIdStr);
                } catch (NumberFormatException e) {
                    log.warn("Could not parse order_id from webhook payload: {}", orderIdStr);
                }
            }

            if (paymentTransactionRepository.findByTransactionId(transactionId).isPresent()) {
                log.info("Idempotent Webhook: Transaction {} already processed. Skipping duplicate.", transactionId);
                return;
            }

            PaymentTransaction tx = new PaymentTransaction();
            tx.setTransactionId(transactionId);
            tx.setOrderId(orderId != null ? orderId : -1L);
            tx.setTransactionType("PAYMENT");
            tx.setAmount(new BigDecimal(paymentEntity.path("amount").asInt()).divide(new BigDecimal("100")));
            tx.setStatus("SUCCESS");
            tx.setWebhookPayload("{\"event\":\"payment.captured\",\"transactionId\":\"" + transactionId + "\"}");
            tx.setCreateTime(OffsetDateTime.now());

            try {
                paymentTransactionRepository.saveAndFlush(tx);
            } catch (DataIntegrityViolationException e) {
                log.info("Idempotent Webhook: Transaction {} already processed concurrently. Skipping duplicate.", transactionId);
                return;
            }

            if (orderId != null) {
                CustomerOrder dbOrder = orderRepository.findById(orderId).orElse(null);
                if (dbOrder != null && dbOrder.getStatus() == 0) {
                    dbOrder.setStatus(1);
                    dbOrder.setPaymentTime(OffsetDateTime.now());
                    orderRepository.save(dbOrder);

                    List<OrderLine> lines = orderLineRepository.findByOrderId(orderId);
                    if (lines != null && !lines.isEmpty()) {
                        stockReservationService.deductStockOnPaymentBatch(lines);
                    }
                }
            }
        } catch (IllegalArgumentException e) {
            throw e;
        } catch (Exception e) {
            log.error("Error processing webhook payload", e);
            throw new RuntimeException("Webhook processing error", e);
        }
    }
}
