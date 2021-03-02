package com.es.phoneshop.exception;

public class ProductNotFoundException extends Exception {
    @Override
    public String toString() {
        return "Product wasn't found";
    }
}
