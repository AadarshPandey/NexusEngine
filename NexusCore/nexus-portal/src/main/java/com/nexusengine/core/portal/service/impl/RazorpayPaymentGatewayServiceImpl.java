package com.nexusengine.core.portal.service.impl;

import com.nexusengine.core.portal.service.RazorpayPaymentGatewayService;
import com.razorpay.RazorpayClient;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class RazorpayPaymentGatewayServiceImpl implements RazorpayPaymentGatewayService {

    @Value("${razorpay.keyId}")
    private String razorpayKeyId;

    @Value("${razorpay.keySecret}")
    private String razorpayKeySecret;

    @Override
    @io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker(name = "razorpay", fallbackMethod = "createOrderFallback")
    @io.github.resilience4j.retry.annotation.Retry(name = "razorpay")
    public Map<String, String> createOrder(int amount, String receipt) {
        try {
            RazorpayClient razorpay = new RazorpayClient(razorpayKeyId, razorpayKeySecret);
            JSONObject orderRequest = new JSONObject();
            orderRequest.put("amount", amount);
            orderRequest.put("currency", "INR");
            orderRequest.put("receipt", receipt);

            com.razorpay.Order razorpayOrder = razorpay.orders.create(orderRequest);

            Map<String, String> result = new HashMap<>();
            result.put("razorpayOrderId", razorpayOrder.get("id"));
            result.put("keyId", razorpayKeyId);
            result.put("amount", String.valueOf(amount));
            return result;
        } catch (Exception e) {
            throw new RuntimeException("Failed to create Razorpay order: " + e.getMessage(), e);
        }
    }

    public Map<String, String> createOrderFallback(int amount, String receipt, Throwable t) {
        System.err.println("Razorpay Fallback Triggered. Underlying Error: " + t.getMessage());
        t.printStackTrace();
        throw new RuntimeException("Payment service temporarily unavailable");
    }

    @Override
    public boolean verifySignature(String orderId, String paymentId, String signature) {
        try {
            JSONObject options = new JSONObject();
            options.put("razorpay_order_id", orderId);
            options.put("razorpay_payment_id", paymentId);
            options.put("razorpay_signature", signature);
            return com.razorpay.Utils.verifyPaymentSignature(options, razorpayKeySecret);
        } catch (Exception e) {
            throw new RuntimeException("Failed to verify signature", e);
        }
    }
}
