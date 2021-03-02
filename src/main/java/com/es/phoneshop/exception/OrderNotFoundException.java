package com.es.phoneshop.exception;

public class OrderNotFoundException extends Exception {
    @Override
    public String toString() {
        return "Order with such Id doesn't exist";
    }
}
