package com.es.phoneshop.model.product.service;

import com.es.phoneshop.model.product.entity.Product;

import java.util.List;

public interface ProductService {
    boolean isProductIgnored(Product product);

    boolean isSortNeeded(String sortField, String sortOrder);

    boolean isQueryNotNullAndNotEmpty(String query);

    List<Product> filterAndSortProducts(List<Product> products, String query, String sortField, String sortOrder);
}