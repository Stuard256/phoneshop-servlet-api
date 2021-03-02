package com.es.phoneshop.model.product.service;

import com.es.phoneshop.model.product.DAO.ArrayListOrderDao;
import com.es.phoneshop.model.product.DAO.OrderDao;
import com.es.phoneshop.model.product.entity.Cart;
import com.es.phoneshop.model.product.entity.CartItem;
import com.es.phoneshop.model.product.entity.Order;
import com.es.phoneshop.model.product.entity.PaymentMethod;
import com.es.phoneshop.model.product.exception.EmptyCartException;
import com.es.phoneshop.model.product.exception.OrderNotFoundException;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class DefaultOrderService implements OrderService {
    private OrderDao orderDao = ArrayListOrderDao.getInstance();

    public static DefaultOrderService getInstance() {
        return DefaultOrderService.SingletonHelper.INSTANCE;
    }

    @Override
    public Order getOrder(Cart cart) throws EmptyCartException {
        Order order = new Order();
        order.setItems(cart.getItems().stream().map(item -> {
            try {
                return (CartItem) item.clone();
            } catch (CloneNotSupportedException e) {
                throw new RuntimeException(e);
            }
        }).collect(Collectors.toList()));
        try {
            order.setSubtotal(cart.getTotalCost());
            order.setDeliveryCost(calculateDeliveryCost());
            order.setTotalCost(order.getSubtotal().add(order.getDeliveryCost()));
        } catch (NullPointerException e) {
            throw new EmptyCartException();
        }
        return order;
    }

    @Override
    public void placeOrder(Order order) throws OrderNotFoundException {
        orderDao.save(order);
    }

    @Override
    public Map<String, String> validate(Order order, String firstName, String lastName, String phone, String deliveryDate, String deliveryAdress, String paymentMethod) {
        Map<String, String> errors = new HashMap<>();

        String regx = "^[\\p{L} .'-]+$";
        Pattern pattern = Pattern.compile(regx, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(firstName);
        if (matcher.find()) {
            order.setFirstName(firstName);
        } else {
            errors.put("firstName", "Check out first name");
        }
        matcher = pattern.matcher(lastName);
        if (matcher.find()) {
            order.setLastName(lastName);
        } else {
            errors.put("lastName", "Check out last name");
        }
        regx = "^((\\+|00)(\\d{1,3})[\\s-]?)?(\\d{10})$";
        Matcher m = Pattern.compile(regx).matcher(phone);
        if (m.matches()) {
            order.setPhone(phone);
        } else {
            errors.put("phone", "Check out phone");
        }

        LocalDate date = LocalDate.parse(deliveryDate);
        if (date.isAfter(LocalDate.now())) {
            order.setDeliveryDate(date);
        } else {
            errors.put("deliveryDate", "Date must be after today");
        }
        if (deliveryAdress == null || deliveryAdress.equals("")) {
            errors.put("deliveryAddress", "Please check your adress");
        } else {
            order.setDeliveryAddress(deliveryAdress);
        }

        if (paymentMethod == null) {
            errors.put("paymentMethod", "Please select payment method");
        } else if (paymentMethod.equals("1")) {
            order.setPaymentMethod(PaymentMethod.CREDIT_CARD);
        } else {
            order.setPaymentMethod(PaymentMethod.CASH);
        }
        return errors;
    }

    private BigDecimal calculateDeliveryCost() {
        return new BigDecimal(5);
    }

    private static class SingletonHelper {
        private static final DefaultOrderService INSTANCE = new DefaultOrderService();
    }
}
