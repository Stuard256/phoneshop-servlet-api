package com.es.phoneshop.model.product.service;

import com.es.phoneshop.model.product.entity.Product;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayDeque;

public interface LastSeenProductsService {
    ArrayDeque<Product> getLastSeen(HttpServletRequest request);

    void addToLastSeen(HttpServletRequest request, Product product);
}
