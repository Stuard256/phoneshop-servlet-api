package com.es.phoneshop.service.order;

import com.es.phoneshop.DAO.Order.ArrayListOrderDao;
import com.es.phoneshop.DAO.Order.OrderDao;
import com.es.phoneshop.entity.cart.Cart;
import com.es.phoneshop.entity.cart.CartItem;
import com.es.phoneshop.entity.order.Order;
import com.es.phoneshop.exception.EmptyCartException;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class DefaultOrderService implements OrderService {
    private final OrderDao orderDao = ArrayListOrderDao.getInstance();

    public static DefaultOrderService getInstance() {
        return DefaultOrderService.SingletonHelper.INSTANCE;
    }

    @Override
    public Order getOrder(Cart cart) throws EmptyCartException {
        if (cart.getItems().isEmpty()) {
            throw new EmptyCartException();
        }
        Order order = new Order();
        try {
            List<CartItem> list = new ArrayList<>();
            for (CartItem cartItem : cart.getItems()) {
                CartItem clone = (CartItem) cartItem.clone();
                list.add(clone);
            }
            order.setItems(list);
            order.setSubtotal(cart.getTotalCost());
            order.setDeliveryCost(calculateDeliveryCost());
            order.setTotalCost(order.getSubtotal().add(order.getDeliveryCost()));
        } catch (NullPointerException e) {
            throw new EmptyCartException();
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }
        return order;
    }

    @Override
    public void placeOrder(Order order) {
        order.setSecureId(UUID.randomUUID().toString());
        orderDao.save(order);
    }

    private BigDecimal calculateDeliveryCost() {
        return new BigDecimal(5);
    }

    private static class SingletonHelper {
        private static final DefaultOrderService INSTANCE = new DefaultOrderService();
    }
}
