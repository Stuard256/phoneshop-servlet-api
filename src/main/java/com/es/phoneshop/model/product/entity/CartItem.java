package com.es.phoneshop.model.product.entity;

import java.io.Serializable;

public class CartItem implements Serializable {
    private Product product;
    private int quantity;

    public CartItem(Product product, int quantity) {
        this.product = product;
        this.quantity = quantity;
    }

    public Product getProduct() {
        return product;
    }

    public int getQuantity() {
        return quantity;
    }

    public void increaseQuantity(int increase) {
        quantity += increase;
    }

    @Override
    public String toString() {
        return "CartItem{" +
                "code=" + product.getCode() +
                ", quantity=" + quantity +
                '}';
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
}
