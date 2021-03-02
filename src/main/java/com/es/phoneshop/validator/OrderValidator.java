package com.es.phoneshop.validator;

import com.es.phoneshop.entity.order.Order;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

public interface OrderValidator {
    Map<String, String> validate(Order order, HttpServletRequest request);
}
