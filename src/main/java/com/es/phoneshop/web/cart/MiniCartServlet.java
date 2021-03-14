package com.es.phoneshop.web.cart;

import com.es.phoneshop.entity.cart.Cart;
import com.es.phoneshop.service.cart.CartService;
import com.es.phoneshop.service.cart.DefaultCartService;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class MiniCartServlet extends HttpServlet {

    protected static final String MINICART_JSP = "/WEB-INF/pages/minicart.jsp";

    private CartService cartService;

    @Override
    public void init(ServletConfig servletConfig) throws ServletException {
        super.init(servletConfig);
        cartService = DefaultCartService.getInstance();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Cart cart = cartService.getCart(request);
        request.setAttribute("cart", cart);
        request.getRequestDispatcher(MINICART_JSP).include(request, response);

    }
}
