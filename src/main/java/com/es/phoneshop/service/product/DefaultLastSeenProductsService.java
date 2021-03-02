package com.es.phoneshop.service.product;

import com.es.phoneshop.entity.product.Product;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayDeque;

public class DefaultLastSeenProductsService implements LastSeenProductsService {
    private static final String LAST_SEEN_PRODUCTS_SESSION_ATTRIBUTE = DefaultLastSeenProductsService.class.getName() + ".products";

    public static DefaultLastSeenProductsService getInstance() {
        return DefaultLastSeenProductsService.SingletonHelper.INSTANCE;
    }

    @Override
    public ArrayDeque<Product> getLastSeen(HttpServletRequest request) {
        ArrayDeque<Product> products = (ArrayDeque<Product>) request.getSession().getAttribute(LAST_SEEN_PRODUCTS_SESSION_ATTRIBUTE);
        if (products == null) {
            request.getSession().setAttribute(LAST_SEEN_PRODUCTS_SESSION_ATTRIBUTE, products = new ArrayDeque<>(3));
        }
        return products;
    }

    @Override
    public void addToLastSeen(HttpServletRequest request, Product product) {
        ArrayDeque<Product> products = getLastSeen(request);
        if (products.contains(product)) {
            return;
        }
        if (products.size() >= 3) {
            products.removeFirst();
        }
        getLastSeen(request).addLast(product);
    }

    private static class SingletonHelper {
        private static final DefaultLastSeenProductsService INSTANCE = new DefaultLastSeenProductsService();
    }
}
