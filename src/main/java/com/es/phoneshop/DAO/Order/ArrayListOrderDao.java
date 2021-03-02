package com.es.phoneshop.DAO.Order;

import com.es.phoneshop.entity.order.Order;
import com.es.phoneshop.exception.OrderNotFoundException;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class ArrayListOrderDao implements OrderDao {
    private final ReadWriteLock lock = new ReentrantReadWriteLock();
    private final List<Order> orders;
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
                    .orElseThrow(OrderNotFoundException::new);
        } finally {
            lock.readLock().unlock();
        }
    }

    @Override
    public Order getOrderBySecureId(String id) throws OrderNotFoundException {
        lock.readLock().lock();
        try {
            return orders.stream()
                    .filter(order -> id.equals(order.getSecureId()))
                    .findAny()
                    .orElseThrow(OrderNotFoundException::new);
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
