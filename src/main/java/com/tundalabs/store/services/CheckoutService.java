package com.tundalabs.store.services;

import com.stripe.exception.StripeException;
import com.stripe.model.checkout.Session;
import com.stripe.param.checkout.SessionCreateParams;
import com.tundalabs.store.dtos.CheckoutRequest;
import com.tundalabs.store.dtos.CheckoutResponse;
import com.tundalabs.store.entities.Order;
import com.tundalabs.store.exceptions.CartEmptyException;
import com.tundalabs.store.exceptions.CartNotFoundException;
import com.tundalabs.store.repositories.CartRepository;
import com.tundalabs.store.repositories.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Map;

@RequiredArgsConstructor
@Service
public class CheckoutService {
    private final CartRepository cartRepository;
    private final OrderRepository orderRepository;
    private final AuthService authService;
    private final CartService cartService;

    @Value("${websiteUrl}")
    private String websiteUrl;


    @Transactional
    public CheckoutResponse checkout(CheckoutRequest request) throws StripeException {
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
            // Create a Checkout Session
            var builder = SessionCreateParams.builder()
                    .setMode(SessionCreateParams.Mode.PAYMENT)
                    .setSuccessUrl(websiteUrl+ "/checkout-sucess?orderId=" + order.getId())
                    .setCancelUrl(websiteUrl+ "/checkout-cancel")
                    .putExtraParam("managed_payments", Map.of("enabled", false));//to disable managed payments

            order.getItems().forEach(item-> {
                var lineItem = SessionCreateParams.LineItem.builder()
                        .setQuantity(Long.valueOf(item.getQuantity()))
                        .setPriceData(
                            SessionCreateParams.LineItem.PriceData.builder()
                                .setCurrency("tzs")
                                .setUnitAmountDecimal(
                                     item.getUnitPrice()
                                        .multiply(BigDecimal.valueOf(100)))
                                .setProductData(
                                     SessionCreateParams.LineItem.PriceData.ProductData.builder()
                                          .setName(item.getProduct().getName())
                                          .build()
                                )
                                .build()
                        )
                        .build();
                builder.addLineItem(lineItem);
            });

            var session = Session.create(builder.build());

            cartService.clearCart(cart.getId());

            return new CheckoutResponse(order.getId(), session.getUrl());
        }
        catch (Exception ex){
            System.out.println(ex.getMessage());
            orderRepository.delete(order);
            throw ex;
        }
    }
}