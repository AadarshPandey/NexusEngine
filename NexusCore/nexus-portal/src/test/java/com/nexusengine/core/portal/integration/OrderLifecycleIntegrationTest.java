package com.nexusengine.core.portal.integration;

import com.nexusengine.core.model.OmsOrder;
import com.nexusengine.core.model.OmsPaymentTransaction;
import com.nexusengine.core.portal.integration.config.TestcontainersConfig;
import com.nexusengine.core.portal.service.OmsPortalOrderService;
import com.nexusengine.core.repository.OmsOrderRepository;
import com.nexusengine.core.repository.OmsPaymentTransactionRepository;
import com.nexusengine.core.repository.PmsSkuStockRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Import(TestcontainersConfig.class)
@ActiveProfiles("test")
@Testcontainers
public class OrderLifecycleIntegrationTest {

    @Autowired
    private OmsPortalOrderService portalOrderService;

    @Autowired
    private OmsOrderRepository orderRepository;

    @Autowired
    private OmsPaymentTransactionRepository paymentTransactionRepository;

    @Autowired
    private PmsSkuStockRepository skuStockRepository;

    @Test
    void paymentWebhook_ValidPayload_CreatesTransactionAndUpdatesOrder() {
        // Create an unpaid order
        OmsOrder order = new OmsOrder();
        order.setStatus(0); // Unpaid
        order = orderRepository.save(order);

        String payload = "{\"event\":\"payment.captured\",\"payload\":{\"payment\":{\"entity\":{\"id\":\"pay_test123\",\"amount\":10000,\"notes\":{\"order_id\":\"" + order.getId() + "\"}}}}}";
        String signature = "test_sig";

        portalOrderService.handlePaymentWebhook(payload, signature);

        // Verify order status updated to paid (1)
        OmsOrder updatedOrder = orderRepository.findById(order.getId()).orElseThrow();
        assertThat(updatedOrder.getStatus()).isEqualTo(1);
        assertThat(updatedOrder.getPaymentId()).isEqualTo("pay_test123");

        // Verify transaction saved
        Optional<OmsPaymentTransaction> tx = paymentTransactionRepository.findByTransactionId("pay_test123");
        assertThat(tx).isPresent();
        assertThat(tx.get().getOrderId()).isEqualTo(order.getId());
    }

    @Test
    void paymentWebhook_DuplicateWebhook_IsIdempotent() {
        // Create an unpaid order
        OmsOrder order = new OmsOrder();
        order.setStatus(0);
        order = orderRepository.save(order);

        String payload = "{\"event\":\"payment.captured\",\"payload\":{\"payment\":{\"entity\":{\"id\":\"pay_duplicate\",\"amount\":10000,\"notes\":{\"order_id\":\"" + order.getId() + "\"}}}}}";
        String signature = "test_sig";

        portalOrderService.handlePaymentWebhook(payload, signature);
        
        // Second call with same transaction ID
        portalOrderService.handlePaymentWebhook(payload, signature);

        // Verify still 1 transaction
        long count = paymentTransactionRepository.findAll().stream().filter(tx -> tx.getTransactionId().equals("pay_duplicate")).count();
        assertThat(count).isEqualTo(1);
    }
}
