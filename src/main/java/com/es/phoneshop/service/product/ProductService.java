package com.es.phoneshop.service.product;

import com.es.phoneshop.entity.product.Product;

import java.util.List;

public interface ProductService {
    boolean isProductIgnored(Product product);

    boolean isSortNeeded(String sortField, String sortOrder);

    boolean isQueryNotNullAndNotEmpty(String query);

    List<Product> filterAndSortProducts(List<Product> products, String query, String sortField, String sortOrder);
}