package com.es.phoneshop.validator;

import com.es.phoneshop.entity.order.Order;
import com.es.phoneshop.entity.order.PaymentMethod;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

public class DefaultOrderValidator implements OrderValidator {
    private Map<String, String> parseParams(HttpServletRequest request) {
        Map<String, String> params = new HashMap<>();
        params.put("firstName", request.getParameter("firstName"));
        params.put("lastName", request.getParameter("lastName"));
        params.put("phone", request.getParameter("phone"));
        params.put("deliveryDate", request.getParameter("deliveryDate"));
        params.put("deliveryAddress", request.getParameter("deliveryAddress"));
        params.put("paymentMethod", request.getParameter("paymentMethod"));
        return params;
    }

    @Override
    public Map<String, String> validate(Order order, HttpServletRequest request) {
        Map<String, String> errors = new HashMap<>();
        Map<String, String> params = parseParams(request);
        for (int i = 0; i < params.size(); i++) {
            params.forEach((k, v) -> {
                if (isValid(k, v)) {
                    switch (k) {
                        case "firstName" -> order.setFirstName(v);
                        case "lastName" -> order.setLastName(v);
                        case "phone" -> order.setPhone(v);
                        case "deliveryDate" -> order.setDeliveryDate(LocalDate.parse(v));
                        case "deliveryAddress" -> order.setDeliveryAddress(v);
                        case "paymentMethod" -> order.setPaymentMethod(PaymentMethod.valueOf(v));
                    }
                } else {
                    errors.put(k, k + ": please enter valid value");
                }
            });
        }
        return errors;
    }

    private boolean isValid(String key, String value) {
        if (value == null || value.isEmpty()) {
            return false;
        }
        String regex;
        switch (key) {
            case "firstName", "lastName" -> regex = "^[\\p{L} .'-]+$";
            case "phone" -> regex = "^((\\+|00)(\\d{1,3})[\\s-]?)?(\\d{10})$";
            default -> regex = "\\s*";
        }
        Pattern pattern = Pattern.compile(regex, Pattern.CASE_INSENSITIVE);
        return pattern.matcher(value).find();
    }
}
