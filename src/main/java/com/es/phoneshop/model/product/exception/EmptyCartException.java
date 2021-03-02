package com.es.phoneshop.model.product.exception;

public class EmptyCartException extends RuntimeException {
    @Override
    public String toString() {
        return "Your cart is empty! Please add something to your cart to continue";
    }
}
