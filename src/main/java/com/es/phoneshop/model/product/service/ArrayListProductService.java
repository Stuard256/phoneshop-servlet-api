package com.es.phoneshop.model.product.service;

import com.es.phoneshop.model.product.entity.Product;
import com.es.phoneshop.model.product.entity.SortField;
import com.es.phoneshop.model.product.entity.SortOrder;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class ArrayListProductService implements ProductService {

    @Override
    public boolean isProductIgnored(Product product) {
        return product.getPrice() == null || product.getStock() <= 0;
    }

    @Override
    public boolean isSortNeeded(String sortField, String sortOrder) {
        return sortField != null && sortOrder != null;
    }

    @Override
    public boolean isQueryNotNullAndNotEmpty(String query) {
        if (query == null) {
            return false;
        } else {
            return !query.isEmpty();
        }
    }

    @Override
    public List<Product> filterAndSortProducts(List<Product> products, String query, String sortField, String sortOrder) {
        Stream<Product> result = products.stream().filter(product -> !isProductIgnored(product));
        if (isQueryNotNullAndNotEmpty(query)) {
            result = result.filter(product -> {
                String[] wordsInQuery = query.trim().toLowerCase().split(" ");
                String[] wordsInDescription = product.getDescription().trim().toLowerCase().split(" ");
                return Arrays.stream(wordsInQuery).anyMatch(word ->
                        Arrays.stream(wordsInDescription).anyMatch(wordInDescription ->
                                wordInDescription.contains(word)));
            });
        }

        if (isSortNeeded(sortField, sortOrder)) {
            SortField field = SortField.valueOf(sortField.toUpperCase());
            SortOrder order = SortOrder.valueOf(sortOrder.toUpperCase());
            Comparator<Product> comparator = Comparator.comparing(product -> {
                if (field == SortField.DESCRIPTION) {
                    return (Comparable) product.getDescription();
                } else {
                    return (Comparable) product.getPrice();
                }
            });
            if (order == SortOrder.DESC) {
                comparator = comparator.reversed();
            }
            result = result.sorted(comparator);
        } else {
            if (isQueryNotNullAndNotEmpty(query)) {
                result = result.sorted(Comparator.comparing(product -> {
                    AtomicInteger atomicInteger = new AtomicInteger();
                    String[] wordsInQuery = query.trim().toLowerCase().split(" ");
                    String[] wordsInDescription = product.getDescription().trim().toLowerCase().split(" ");

                    Arrays.stream(wordsInQuery).forEach(word ->
                            Arrays.stream(wordsInDescription).forEach(wordInDescription -> {
                                if (wordInDescription.contains(word)) {
                                    atomicInteger.getAndDecrement();
                                }
                            }));
                    return atomicInteger.get();
                }));
            }
        }
        return result.collect(Collectors.toList());
    }
}
