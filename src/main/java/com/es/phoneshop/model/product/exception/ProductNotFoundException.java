package com.es.phoneshop.model.product.exception;

import java.util.function.Supplier;

public class ProductNotFoundException extends Exception{
    public ProductNotFoundException(){}
    @Override
    public String getMessage() {
        return "Product wasn't found";
    }
}
