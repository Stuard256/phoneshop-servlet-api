package com.es.phoneshop.model.product.DAO;

import com.es.phoneshop.model.product.entity.Product;
import com.es.phoneshop.model.product.entity.SortField;
import com.es.phoneshop.model.product.entity.SortOrder;
import com.es.phoneshop.model.product.exception.ProductNotFoundException;
import com.es.phoneshop.model.product.service.ArrayListProductService;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class ArrayListProductDao implements ProductDao {
    private volatile static ArrayListProductDao instance;
    private final ReadWriteLock lock = new ReentrantReadWriteLock();
    private long maxId;
    private List<Product> products;

    private ArrayListProductDao() {
    }

    public static ArrayListProductDao getInstance() {
        if (instance == null) {
            synchronized (ArrayListProductDao.class) {
                if (instance == null) {
                    instance = new ArrayListProductDao();
                    instance.initialize();
                }
            }
        }
        return instance;
    }

    private void initialize() {
        this.products = new ArrayList<>();
    }

    @Override
    public Product getProduct(Long id) throws ProductNotFoundException {
        lock.readLock().lock();
        try {
            return products.stream()
                    .filter(product -> id.equals(product.getId()))
                    .filter(product -> product.getPrice() != null)
                    .filter(product -> product.getStock() > 0)
                    .findAny()
                    .orElseThrow(ProductNotFoundException::new);
        } finally {
            lock.readLock().unlock();
        }
    }

    @Override
    public List<Product> findProducts(String query, String sortField, String sortOrder) {
        lock.readLock().lock();
        try {
            Stream<Product> result;
            ArrayListProductService service = new ArrayListProductService();
            if (sortField == null || sortOrder == null) {
                if (query != null) {
                    if (query.isEmpty()) {
                        result = products.stream()
                                .filter(product -> product.getPrice() != null)
                                .filter(product -> product.getStock() > 0);
                    } else {
                        result = products.stream()
                                .filter(product -> service.containsWords(product.getDescription(), query))
                                .filter(product -> product.getPrice() != null)
                                .filter(product -> product.getStock() > 0)
                                .sorted(Comparator.comparing(product -> service.matchWords(product.getDescription(), query)));
                    }
                } else {
                    result = products.stream()
                            .filter(product -> product.getPrice() != null)
                            .filter(product -> product.getStock() > 0);
                }
            } else {
                SortField field = SortField.valueOf(sortField);
                SortOrder order = SortOrder.valueOf(sortOrder);
                Comparator<Product> comparator = Comparator.comparing(product -> {
                    if (SortField.description == field) {
                        return (Comparable) product.getDescription();
                    } else {
                        return (Comparable) product.getPrice();
                    }
                });
                if (order == SortOrder.desc) {
                    comparator = comparator.reversed();
                }
                result = products.stream()
                        .filter(product -> query == null || query.isEmpty() || service.containsWords(product.getDescription(), query))
                        .filter(product -> product.getPrice() != null)
                        .filter(product -> product.getStock() > 0)
                        .sorted(comparator);
            }
            return result.collect(Collectors.toList());
        } finally {
            lock.readLock().unlock();
        }
    }

    @Override
    public void save(Product product) {
        lock.writeLock().lock();
        try {
            if (product.getId() != null) {
                delete(product.getId());
            } else {
                product.setId(maxId++);
            }
            products.add(product);
        } finally {
            lock.writeLock().unlock();
        }
    }

    @Override
    public void delete(Long id) {
        lock.writeLock().lock();
        products.removeIf(product -> product.getId().equals(id));
        lock.writeLock().unlock();
    }
}
