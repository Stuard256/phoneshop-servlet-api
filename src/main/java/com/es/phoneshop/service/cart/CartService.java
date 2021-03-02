package com.es.phoneshop.service.cart;

import com.es.phoneshop.entity.cart.Cart;
import com.es.phoneshop.exception.OutOfStockException;
import com.es.phoneshop.exception.ProductNotFoundException;

import javax.servlet.http.HttpServletRequest;

public interface CartService {
    Cart getCart(HttpServletRequest request);
    void add(Cart cart, Long productId, int quantity) throws OutOfStockException, ProductNotFoundException;
    void update(Cart cart, Long productId, int quantity) throws OutOfStockException, ProductNotFoundException;
    void delete(Cart cart, Long productId);
    void clear(Cart cart);
}
