package com.tundalabs.store.carts;

public class CartNotFoundException extends RuntimeException{
    public CartNotFoundException(){
        super("Cart Not Found");
    }
}
