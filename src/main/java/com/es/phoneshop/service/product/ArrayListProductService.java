package com.es.phoneshop.service.product;

import com.es.phoneshop.entity.product.Product;
import com.es.phoneshop.entity.product.SortField;
import com.es.phoneshop.entity.product.SortOrder;
import com.es.phoneshop.exception.InvalidParamException;

import java.math.BigDecimal;
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
    public boolean isSortParamNotEmpty(String sortParam) {
        if(sortParam != null){
            return !sortParam.isEmpty();
        }
        return false;
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
        if (isSortParamNotEmpty(sortField)&& isSortParamNotEmpty(sortOrder)) {
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

    @Override
    public List<Product> advancedSearchOfProducts(List<Product> products, String query, String minPrice, String maxPrice, String searchOption) throws InvalidParamException {
        List<Product> result = filterAndSortProducts(products,query,null,null);
        if(isSortParamNotEmpty(minPrice)) {
            try {
                BigDecimal min = new BigDecimal(minPrice.replace(',', '.')).setScale(2, BigDecimal.ROUND_DOWN);
                result = result.stream().filter(product -> product.getPrice().compareTo(min)>=0).collect(Collectors.toList());
            } catch (NumberFormatException e) {
                throw new InvalidParamException("MIN");
            }
        }
        if(isSortParamNotEmpty(maxPrice)){
            try{
                BigDecimal max = new BigDecimal(maxPrice.replace(',', '.')).setScale(2, BigDecimal.ROUND_DOWN);
                result = result.stream().filter(product -> product.getPrice().compareTo(max)<=0).collect(Collectors.toList());
            }catch (NumberFormatException e){
                throw new InvalidParamException("MAX");
            }
        }
        return result;
    }
}
