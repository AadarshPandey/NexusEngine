package com.nexusengine.core.portal.ordering.integration;

import com.nexusengine.core.domain.ordering.entity.CustomerOrder;
import com.nexusengine.core.domain.payment.entity.PaymentTransaction;
import com.nexusengine.core.portal.integration.config.TestcontainersConfig;
import com.nexusengine.core.portal.ordering.service.OrderService;
import com.nexusengine.core.domain.ordering.repository.CustomerOrderRepository;
import com.nexusengine.core.domain.payment.repository.PaymentTransactionRepository;
import com.nexusengine.core.domain.catalog.repository.ProductSkuRepository;
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

    private final OrderService portalOrderService;
    private final CustomerOrderRepository orderRepository;
    private final PaymentTransactionRepository paymentTransactionRepository;
    private final ProductSkuRepository skuStockRepository;

    @Autowired
    OrderLifecycleIntegrationTest(OrderService portalOrderService, CustomerOrderRepository orderRepository, PaymentTransactionRepository paymentTransactionRepository, ProductSkuRepository skuStockRepository) {
        this.portalOrderService = portalOrderService;
        this.orderRepository = orderRepository;
        this.paymentTransactionRepository = paymentTransactionRepository;
        this.skuStockRepository = skuStockRepository;
    }






    @Test
    void paymentWebhook_ValidPayload_CreatesTransactionAndUpdatesOrder() {
        // Create an unpaid order
        CustomerOrder order = new CustomerOrder();
        order.setStatus(0); // Unpaid
        order = orderRepository.save(order);

        String payload = "{\"event\":\"payment.captured\",\"payload\":{\"payment\":{\"entity\":{\"id\":\"pay_test123\",\"amount\":10000,\"notes\":{\"order_id\":\"" + order.getId() + "\"}}}}}";
        String signature = "test_sig";

        portalOrderService.handlePaymentWebhook(payload, signature);

        // Verify order status updated to paid (1)
        CustomerOrder updatedOrder = orderRepository.findById(order.getId()).orElseThrow();
        assertThat(updatedOrder.getStatus()).isEqualTo(1);

        // Verify transaction saved
        Optional<PaymentTransaction> tx = paymentTransactionRepository.findByTransactionId("pay_test123");
        assertThat(tx).isPresent();
        assertThat(tx.get().getOrderId()).isEqualTo(order.getId());
    }

    @Test
    void paymentWebhook_DuplicateWebhook_IsIdempotent() {
        // Create an unpaid order
        CustomerOrder order = new CustomerOrder();
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
