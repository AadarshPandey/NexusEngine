package com.nexusengine.core.portal.payment.service;

import com.nexusengine.core.portal.ordering.dto.PaymentInitiationResponse;

public interface PaymentGatewayService {

    PaymentInitiationResponse createOrder(int amount, String receipt);

    boolean verifySignature(String orderId, String paymentId, String signature);

    boolean verifyPaymentAmount(String paymentId, int expectedAmount);

    boolean verifyWebhookSignature(String payload, String signature);
}
