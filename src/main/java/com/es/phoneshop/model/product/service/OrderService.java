package com.es.phoneshop.model.product.service;

import com.es.phoneshop.model.product.entity.Cart;
import com.es.phoneshop.model.product.entity.Order;
import com.es.phoneshop.model.product.exception.EmptyCartException;
import com.es.phoneshop.model.product.exception.OrderNotFoundException;

import java.util.Map;

public interface OrderService {
    Order getOrder(Cart cart) throws EmptyCartException;
    void placeOrder(Order order) throws OrderNotFoundException;
    Map<String,String> validate(Order order, String firstName, String lastName, String phone, String deliveryDate, String deliveryAdress, String paymentMethod);
}
