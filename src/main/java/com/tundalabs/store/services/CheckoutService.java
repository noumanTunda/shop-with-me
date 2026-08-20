package com.tundalabs.store.services;

import com.tundalabs.store.dtos.CheckoutRequest;
import com.tundalabs.store.dtos.CheckoutResponse;
import com.tundalabs.store.entities.Order;
import com.tundalabs.store.entities.PaymentStatus;
import com.tundalabs.store.exceptions.CartEmptyException;
import com.tundalabs.store.exceptions.CartNotFoundException;
import com.tundalabs.store.exceptions.PaymentException;
import com.tundalabs.store.repositories.CartRepository;
import com.tundalabs.store.repositories.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@RequiredArgsConstructor
@Service
public class CheckoutService {
    private final CartRepository cartRepository;
    private final OrderRepository orderRepository;
    private final AuthService authService;
    private final CartService cartService;
    private final PaymentGateway paymentGateway;


    @Transactional
    public CheckoutResponse checkout(CheckoutRequest request){
        var cart = cartRepository.getCartWithItems(request.getCartId()).orElse(null);
        if (cart == null){
            throw new CartNotFoundException();
        }

        if(cart.isEmpty()){
            throw new CartEmptyException();
        }

        var order = Order.fromCart(cart,authService.getCurrentUser());

        orderRepository.save(order);

        try{
            var session = paymentGateway.createCheckoutSession(order);
            cartService.clearCart(cart.getId());

            return new CheckoutResponse(order.getId(), session.getCheckoutUrl());
        }
        catch (PaymentException ex){
            orderRepository.delete(order);
            throw ex;
        }
    }

    public void handleWebhookEvent(WebhookRequest request){
        paymentGateway
                .parseWebhookRequest(request)
                .ifPresent(paymentResult -> {
                    var order = orderRepository.findById(paymentResult.getOrderId()).orElseThrow();//cjabadili
                    order.setStatus(paymentResult.getPaymentStatus());
                    System.out.println(order.getStatus());//debug
//                    order.setStatus(PaymentStatus.PAID);
                    orderRepository.save(order);
                    System.out.println(order.getStatus());//debug
                });
    }
}