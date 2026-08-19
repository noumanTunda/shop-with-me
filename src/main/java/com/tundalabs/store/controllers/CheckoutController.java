package com.tundalabs.store.controllers;

import com.stripe.exception.StripeException;
import com.tundalabs.store.dtos.CheckoutRequest;
import com.tundalabs.store.dtos.ErrorDto;
import com.tundalabs.store.exceptions.CartEmptyException;
import com.tundalabs.store.exceptions.CartNotFoundException;
import com.tundalabs.store.services.CheckoutService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@AllArgsConstructor
@RestController
@RequestMapping("/checkout")
public class CheckoutController {
    private final CheckoutService checkoutService;


    @PostMapping
    public ResponseEntity<?> checkout(
           @Valid @RequestBody CheckoutRequest request
    ){
        try{
            return ResponseEntity.ok(checkoutService.checkout(request));
        }
        catch (StripeException ex){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ErrorDto("Checkout Service is Experiencing Issues"));
        }
    }

    @ExceptionHandler({CartNotFoundException.class, CartEmptyException.class})
    public ResponseEntity<ErrorDto> handleException(Exception ex){
        return ResponseEntity.badRequest().body(new ErrorDto(ex.getMessage()));
    }
}
