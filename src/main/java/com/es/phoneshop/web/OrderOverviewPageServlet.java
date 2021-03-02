package com.es.phoneshop.web;

import com.es.phoneshop.model.product.DAO.ArrayListOrderDao;
import com.es.phoneshop.model.product.DAO.OrderDao;
import com.es.phoneshop.model.product.exception.OrderNotFoundException;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class OrderOverviewPageServlet extends HttpServlet {

    protected static final String OVERVIEW_JSP = "/WEB-INF/pages/overview.jsp";
    private OrderDao orderDao;

    @Override
    public void init(ServletConfig servletConfig) throws ServletException {
        super.init(servletConfig);
        orderDao = ArrayListOrderDao.getInstance();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws
            ServletException, IOException {

        String orderId = request.getPathInfo().substring(1);
        Long id = Long.valueOf(orderId);
        try {
            if (orderDao.getOrder(id) == null) {
                throw new OrderNotFoundException();
            }
        } catch (OrderNotFoundException e) {
            request.setAttribute("error", e.toString());
            request.getRequestDispatcher("/WEB-INF/pages/error.jsp").forward(request, response);
            return;
        }
        request.setAttribute("order", orderDao.getOrder(id));
        request.getRequestDispatcher(OVERVIEW_JSP).forward(request, response);
    }

}
