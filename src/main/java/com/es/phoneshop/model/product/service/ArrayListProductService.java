package com.es.phoneshop.model.product.service;

import com.es.phoneshop.model.product.entity.Product;
import com.es.phoneshop.model.product.entity.SortField;
import com.es.phoneshop.model.product.entity.SortOrder;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Stream;

public class ArrayListProductService implements ProductService {

    public boolean isProductIgnored(Product product) {
        return product.getPrice() == null || product.getStock() <= 0;
    }

    public boolean isSortNeeded(String sortField, String sortOrder) {
        return sortField != null && sortOrder != null;
    }

    public boolean isQueryNotNullAndNotEmpty(String query) {
        if (query == null) {
            return false;
        } else return !query.isEmpty();
    }

    public Stream<Product> sortProducts(Stream<Product> products, SortField field, SortOrder order) {
        Comparator<Product> comparator = Comparator.comparing(product -> {
            if (field == SortField.description) {
                return (Comparable) product.getDescription();
            } else {
                return (Comparable) product.getPrice();
            }
        });
        if (order == SortOrder.desc) {
            comparator = comparator.reversed();
        }
        return products.sorted(comparator);
    }

    public boolean containsWords(String description, String query) {
        query = query.trim().toLowerCase();
        description = description.trim().toLowerCase();
        String[] wordsInQuery = query.split(" ");
        String[] wordsInDescription = description.split(" ");
        for (String wordInQuery : wordsInQuery) {
            for (String wordInDescription : wordsInDescription) {
                if (wordInDescription.contains(wordInQuery)) {
                    return true;
                }
            }
        }
        return false;
    }

    public int matchWords(String description, String query) {
        int result = 0;
        query = query.trim().toLowerCase();
        description = description.trim().toLowerCase();
        String[] wordsInQuery = query.split(" ");
        String[] wordsInDescription = description.split(" ");
        for (String wordInQuery : wordsInQuery) {
            for (String wordInDescription : wordsInDescription) {
                if (wordInDescription.equals(wordInQuery)) {
                    result--;
                    break;
                }
            }
        }
        return result;
    }
}
