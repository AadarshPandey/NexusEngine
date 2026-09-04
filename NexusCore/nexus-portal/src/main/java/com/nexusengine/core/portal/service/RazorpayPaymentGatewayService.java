package com.nexusengine.core.portal.service;

import java.util.Map;

public interface RazorpayPaymentGatewayService {
    Map<String, String> createOrder(int amount, String receipt);
    boolean verifySignature(String orderId, String paymentId, String signature);
}
