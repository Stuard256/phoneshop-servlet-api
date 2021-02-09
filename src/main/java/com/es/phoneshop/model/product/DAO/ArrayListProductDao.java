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
        } catch (ProductNotFoundException e) {
            return null;
        } finally {
            lock.readLock().unlock();
        }
    }

    @Override
    public List<Product> findProducts() {
        return findProducts(null, null, null);
    }

    @Override
    public List<Product> findProducts(String query) {
        return findProducts(query, null, null);
    }

    @Override
    public List<Product> findProducts(String query, String sortField, String sortOrder) {
        lock.readLock().lock();
        try {
            ArrayListProductService service = new ArrayListProductService();
            Stream<Product> result = products.stream().filter(product -> !service.isProductIgnored(product));
            if (service.isQueryNotNullAndNotEmpty(query)) {
                {
                    result = result
                            .filter(product -> service.containsWords(product.getDescription(), query))
                            .sorted(Comparator.comparing(product -> service.matchWords(product.getDescription(), query)));
                }
            }
            if (service.isSortNeeded(sortField, sortOrder)) {
                SortField field = SortField.valueOf(sortField);
                SortOrder order = SortOrder.valueOf(sortOrder);
                result = service.sortProducts(result,field,order);
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
