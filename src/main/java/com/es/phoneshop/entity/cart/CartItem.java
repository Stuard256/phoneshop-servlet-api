package com.es.phoneshop.entity.cart;

import com.es.phoneshop.entity.product.Product;

import java.io.Serializable;

public class CartItem implements Serializable, Cloneable {
    private final Product product;
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

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public void increaseQuantity(int increase) {
        quantity += increase;
    }

    @Override
    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }

    @Override
    public String toString() {
        return "CartItem{" +
                "code=" + product.getCode() +
                ", quantity=" + quantity +
                '}';
    }

}
