package com.es.phoneshop.service.product;

import com.es.phoneshop.entity.product.Product;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayDeque;

public interface LastSeenProductsService {
    ArrayDeque<Product> getLastSeen(HttpServletRequest request);

    void addToLastSeen(HttpServletRequest request, Product product);
}
