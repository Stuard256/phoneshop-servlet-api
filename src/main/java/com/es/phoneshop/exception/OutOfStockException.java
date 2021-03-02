package com.es.phoneshop.exception;

import com.es.phoneshop.entity.product.Product;

public class OutOfStockException extends Exception {
    private final Product product;
    private final int stockRequested;
    private final int stockAvailable;

    public OutOfStockException(Product product, int stockRequested, int stockAvailable) {
        this.product = product;
        this.stockRequested = stockRequested;
        this.stockAvailable = stockAvailable;
    }


    @Override
    public String toString() {
        if (this.getStockRequested() > 0) {
            return "Product " + this.getProduct().getDescription() + " is gonna be out of stock. Available: " + this.getStockAvailable() + " ,wanted: " + this.getStockRequested();
        } else {
            return "Quantity must be greater than zero";
        }
    }

    public Product getProduct() {
        return product;
    }

    public int getStockRequested() {
        return stockRequested;
    }

    public int getStockAvailable() {
        return stockAvailable;
    }
}
