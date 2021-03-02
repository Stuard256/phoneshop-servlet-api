package com.es.phoneshop.web.order;

import com.es.phoneshop.entity.cart.Cart;
import com.es.phoneshop.entity.order.Order;
import com.es.phoneshop.exception.EmptyCartException;
import com.es.phoneshop.service.cart.CartService;
import com.es.phoneshop.service.cart.DefaultCartService;
import com.es.phoneshop.service.order.DefaultOrderService;
import com.es.phoneshop.service.order.OrderService;
import com.es.phoneshop.validator.DefaultOrderValidator;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;

public class CheckoutPageServlet extends HttpServlet {

    protected static final String CHECKOUT_JSP = "/WEB-INF/pages/checkout.jsp";
    private CartService cartService;
    private OrderService orderService;

    @Override
    public void init(ServletConfig servletConfig) throws ServletException {
        super.init(servletConfig);
        cartService = DefaultCartService.getInstance();
        orderService = DefaultOrderService.getInstance();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws
            ServletException, IOException {
        Cart cart = cartService.getCart(request);
        try {
            request.setAttribute("order", orderService.getOrder(cart));
        } catch (EmptyCartException e) {
            response.sendRedirect(request.getContextPath() + "/products?error=" + e.toString());
            return;
        }
        request.getRequestDispatcher(CHECKOUT_JSP).forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Cart cart = cartService.getCart(request);
        Map<String, String> errors;
        Order order = null;
        try {
            order = orderService.getOrder(cart);
        } catch (EmptyCartException e) {
            response.sendRedirect(request.getContextPath() + "/products?error=" + e.toString());
        }
        errors = new DefaultOrderValidator().validate(order, request);
        if (errors.isEmpty()) {
            orderService.placeOrder(order);
            cartService.clear(cart);
            assert order != null;
            response.sendRedirect(request.getContextPath() + "/order/overview/" + order.getSecureId());
        } else {
            request.setAttribute("errors", errors);
            request.setAttribute("order", order);
            request.getRequestDispatcher(CHECKOUT_JSP).forward(request, response);
        }
    }
}
