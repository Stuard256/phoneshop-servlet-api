package com.es.phoneshop.service.product;

import com.es.phoneshop.entity.product.Product;
import com.es.phoneshop.exception.InvalidParamException;

import java.util.List;

public interface ProductService {
    boolean isProductIgnored(Product product);

    boolean isSortParamNotEmpty(String sortParam);

    boolean isQueryNotNullAndNotEmpty(String query);

    List<Product> filterAndSortProducts(List<Product> products, String query, String sortField, String sortOrder);

    List<Product> advancedSearchOfProducts(List<Product> products, String query, String minPrice, String maxPrice, String searchOption) throws InvalidParamException;
}