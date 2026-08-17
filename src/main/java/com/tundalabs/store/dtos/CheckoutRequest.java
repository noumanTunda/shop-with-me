package com.tundalabs.store.dtos;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.UUID;
@Data
public class CheckoutRequest {
    @NotNull(message = "CartId is Required")
    private UUID cartId;
}
