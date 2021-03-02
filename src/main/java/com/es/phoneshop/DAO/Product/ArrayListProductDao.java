package com.es.phoneshop.DAO.Product;

import com.es.phoneshop.entity.product.Product;
import com.es.phoneshop.exception.ProductNotFoundException;
import com.es.phoneshop.service.product.ArrayListProductService;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

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
                    .findAny()
                    .orElseThrow(ProductNotFoundException::new);
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
            return service.filterAndSortProducts(products, query, sortField, sortOrder);
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
