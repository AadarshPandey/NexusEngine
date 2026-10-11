package com.nexusengine.core.portal.payment.service;

import java.util.Map;



public interface PaymentGatewayService {
    Map<String, String> createOrder(int amount, String receipt);
    boolean verifySignature(String orderId, String paymentId, String signature);
    boolean verifyPaymentAmount(String paymentId, int expectedAmount);
}
