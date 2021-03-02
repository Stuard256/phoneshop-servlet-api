package com.es.phoneshop.web;

import com.es.phoneshop.model.product.DAO.ArrayListProductDao;
import com.es.phoneshop.model.product.DAO.ProductDao;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class ProductListPageServlet extends HttpServlet {

    protected static final String PRODUCT_LIST_JSP = "/WEB-INF/pages/productList.jsp";
    private ProductDao productDao;

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        productDao = ArrayListProductDao.getInstance();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String query = request.getParameter("query");
        String sortField = request.getParameter("field");
        String sortOrder = request.getParameter("order");
        request.setAttribute("products", productDao.findProducts(query, sortField, sortOrder));
        request.getRequestDispatcher(PRODUCT_LIST_JSP).forward(request, response);
    }
}
