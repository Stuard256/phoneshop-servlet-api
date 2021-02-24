package com.es.phoneshop.model.product.entity;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class Cart {
    private List<CartItem> items;

    public Cart() {
        this.items = new ArrayList<>();
    }

    public List<CartItem> getItems() {
        return items;
    }

    public CartItem getItem(Product product) {
        Optional<CartItem> found = items.stream().filter(item -> item.getProduct().equals(product)).findAny();
        return found.orElse(null);
    }

    public void addItem(CartItem cartItem) {
        Optional<CartItem> found = items.stream().filter(item -> item.getProduct().equals(cartItem.getProduct())).findAny();
        if (found.isPresent()) {
            found.get().increaseQuantity(cartItem.getQuantity());
        } else {
            items.add(cartItem);
        }
    }

    @Override
    public String toString() {
        return "Cart{" +
                "items=" + items +
                '}';
    }
}
