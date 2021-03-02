package com.es.phoneshop.service.product;

import com.es.phoneshop.entity.product.Product;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.Date;
import java.util.List;

public class PriceHistoryService {
    public void addToPriceHistory(Product product, BigDecimal price) {
        List<Product.PriceAndDate> priceHistory = product.getPriceHistory();
        priceHistory.add(new Product.PriceAndDate(price, Date.from(Instant.now())));
    }
}
