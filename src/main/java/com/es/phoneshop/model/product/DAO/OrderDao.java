package com.es.phoneshop.model.product.DAO;

import com.es.phoneshop.model.product.entity.Order;
import com.es.phoneshop.model.product.entity.Product;
import com.es.phoneshop.model.product.exception.OrderNotFoundException;
import com.es.phoneshop.model.product.exception.ProductNotFoundException;

import java.util.List;

public interface OrderDao {
    Order getOrder(Long id) throws OrderNotFoundException;
    void save(Order order) throws OrderNotFoundException;
    void delete(Long id);
}
