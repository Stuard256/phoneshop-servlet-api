package com.es.phoneshop.model.product.service;

import com.es.phoneshop.model.product.entity.Cart;
import com.es.phoneshop.model.product.exception.OutOfStockException;
import com.es.phoneshop.model.product.exception.ProductNotFoundException;

import javax.servlet.http.HttpServletRequest;

public interface CartService {
    Cart getCart(HttpServletRequest request);
    void add(Cart cart, Long productId, int quantity) throws OutOfStockException, ProductNotFoundException;
    void update(Cart cart, Long productId, int quantity) throws OutOfStockException, ProductNotFoundException;
    void delete(Cart cart, Long productId);
}
