package com.tundalabs.store.services;

import com.tundalabs.store.entities.Order;

import java.util.Optional;

public interface PaymentGateway {
    CheckoutSession createCheckoutSession (Order order);

    Optional<PaymentResult> parseWebhookRequest(WebhookRequest request);
}
