package com.es.phoneshop.DAO.Product;

import com.es.phoneshop.entity.product.Product;
import com.es.phoneshop.exception.InvalidParamException;
import com.es.phoneshop.exception.ProductNotFoundException;

import java.util.List;

public interface ProductDao {
    Product getProduct(Long id) throws ProductNotFoundException;

    List<Product> findProducts();

    List<Product> findProducts(String query);

    List<Product> findProducts(String query, String sortField, String sortOrder);

    List<Product> advancedSearch(String query, String minPrice, String maxPrice, String searchOption) throws InvalidParamException;

    void save(Product product);

    void delete(Long id);
}
