package com.tundalabs.store.services;

import com.tundalabs.store.entities.Order;

public interface PaymentGateway {
    CheckoutSession createCheckoutSession (Order order);
}
