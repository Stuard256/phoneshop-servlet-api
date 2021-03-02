package com.es.phoneshop.web.order;

import com.es.phoneshop.DAO.Order.ArrayListOrderDao;
import com.es.phoneshop.DAO.Order.OrderDao;
import com.es.phoneshop.exception.OrderNotFoundException;

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
        String secureOrderId = request.getPathInfo().substring(1);
        try {
            request.setAttribute("order", orderDao.getOrderBySecureId(secureOrderId));
        } catch (OrderNotFoundException e) {
            request.setAttribute("error", e.toString());
            request.getRequestDispatcher("/WEB-INF/pages/error.jsp").forward(request, response);
            return;
        }
        request.getRequestDispatcher(OVERVIEW_JSP).forward(request, response);
    }

}
