package com.es.phoneshop.model.product.DAO;

import com.es.phoneshop.model.product.entity.Product;
import com.es.phoneshop.model.product.entity.SortField;
import com.es.phoneshop.model.product.entity.SortOrder;
import com.es.phoneshop.model.product.exception.ProductNotFoundException;

import java.util.List;

public interface ProductDao {
    Product getProduct(Long id) throws ProductNotFoundException;

    List<Product> findProducts();

    List<Product> findProducts(String query);

    List<Product> findProducts(String query, String sortField, String sortOrder);
    void save(Product product) throws ProductNotFoundException;
    void delete(Long id) throws ProductNotFoundException;
}
