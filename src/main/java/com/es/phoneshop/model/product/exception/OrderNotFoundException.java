package com.es.phoneshop.model.product.exception;

public class OrderNotFoundException extends RuntimeException {
    @Override
    public String toString() {
        return "Order with such Id doesn't exist";
    }
}
