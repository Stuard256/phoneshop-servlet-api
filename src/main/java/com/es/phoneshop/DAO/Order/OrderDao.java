package com.es.phoneshop.DAO.Order;

import com.es.phoneshop.entity.order.Order;
import com.es.phoneshop.exception.OrderNotFoundException;

public interface OrderDao {
    Order getOrder(Long id) throws OrderNotFoundException;

    Order getOrderBySecureId(String id) throws OrderNotFoundException;

    void save(Order order);

    void delete(Long id);
}
