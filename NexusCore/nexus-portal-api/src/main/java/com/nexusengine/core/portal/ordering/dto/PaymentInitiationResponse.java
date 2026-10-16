package com.nexusengine.core.portal.ordering.dto;

public record PaymentInitiationResponse(String razorpayOrderId, String keyId, String amount) {}
