package com.es.phoneshop.model.product.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class Cart implements Serializable {
    private List<CartItem> items;

    public List<CartItem> getItems() {
        return items;
    }

    public int getTotalQuantity() {
        return totalQuantity;
    }

    public void setTotalQuantity(int totalQuantity) {
        this.totalQuantity = totalQuantity;
    }

    public BigDecimal getTotalCost() {
        return totalCost;
    }

    public void setTotalCost(BigDecimal totalCost) {
        this.totalCost = totalCost;
    }

    private int totalQuantity;
    private BigDecimal totalCost;

    public Cart() {
        this.items = new ArrayList<>();
    }

    @Override
    public String toString() {
        return "Cart:" +
                "items:" + items;
    }
}
