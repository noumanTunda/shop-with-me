package com.tundalabs.store.controllers;

import com.tundalabs.store.dtos.CheckoutRequest;
import com.tundalabs.store.dtos.CheckoutResponse;
import com.tundalabs.store.dtos.ErrorDto;
import com.tundalabs.store.entities.Order;
import com.tundalabs.store.entities.OrderItem;
import com.tundalabs.store.entities.OrderStatus;
import com.tundalabs.store.repositories.AddressRepository1;
import com.tundalabs.store.repositories.CartRepository;
import com.tundalabs.store.services.AuthService;
import com.tundalabs.store.services.CartService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@AllArgsConstructor
@RestController
@RequestMapping("/checkout")
public class CheckoutController {
    private final CartRepository cartRepository;
    private final AuthService authService;
    private final AddressRepository1 orderRepository;
    private final CartService cartService;


    @PostMapping
    public ResponseEntity<?> checkout(
           @Valid @RequestBody CheckoutRequest request
    ){
        var cart = cartRepository.getCartWithItems(request.getCartId()).orElse(null);
        if (cart == null){
            return ResponseEntity.badRequest().body(
                    new ErrorDto("Cart Not Found")
            );
        }

        if(cart.getItems().isEmpty()){
            return ResponseEntity.badRequest().body(
                    new ErrorDto("Cart is Empty")
            );
        }

        var order = Order.fromCart(cart,authService.getCurrentUser());

        orderRepository.save(order);

        cartService.clearCart(cart.getId());

        return ResponseEntity.ok(new CheckoutResponse(order.getId()));
    }
}
