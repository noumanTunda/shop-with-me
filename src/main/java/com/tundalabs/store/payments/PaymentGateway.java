package com.tundalabs.store.payments;

import com.tundalabs.store.orders.Order;

import java.util.Optional;

public interface PaymentGateway {
    CheckoutSession createCheckoutSession (Order order);

    Optional<PaymentResult> parseWebhookRequest(WebhookRequest request);
}
