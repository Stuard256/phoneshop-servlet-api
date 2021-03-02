package com.es.phoneshop.model.product.DAO;

import com.es.phoneshop.model.product.entity.Order;
import com.es.phoneshop.model.product.exception.OrderNotFoundException;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class ArrayListOrderDao implements OrderDao {
    private final ReadWriteLock lock = new ReentrantReadWriteLock();
    private List<Order> orders;
    private long maxId;

    private ArrayListOrderDao() {
        orders = new ArrayList<>();
    }

    public static ArrayListOrderDao getInstance() {
        return ArrayListOrderDao.SingletonHelper.INSTANCE;
    }

    @Override
    public Order getOrder(Long id) throws OrderNotFoundException {
        lock.readLock().lock();
        try {
            return orders.stream()
                    .filter(order -> id.equals(order.getId()))
                    .findAny()
                    .orElse(null);
        } finally {
            lock.readLock().unlock();
        }
    }

    @Override
    public void save(Order order) {
        lock.writeLock().lock();
        try {
            if (order.getId() != null) {
                delete(order.getId());
            } else {
                order.setId(maxId++);
            }
            orders.add(order);
        } finally {
            lock.writeLock().unlock();
        }
    }

    @Override
    public void delete(Long id) {
        lock.writeLock().lock();
        orders.removeIf(order -> order.getId().equals(id));
        lock.writeLock().unlock();
    }

    private static class SingletonHelper {
        private static final ArrayListOrderDao INSTANCE = new ArrayListOrderDao();
    }
}
