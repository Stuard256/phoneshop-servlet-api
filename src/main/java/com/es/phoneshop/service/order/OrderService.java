package com.es.phoneshop.service.order;

import com.es.phoneshop.entity.cart.Cart;
import com.es.phoneshop.entity.order.Order;
import com.es.phoneshop.exception.EmptyCartException;

public interface OrderService {
    Order getOrder(Cart cart) throws EmptyCartException;

    void placeOrder(Order order);
}
