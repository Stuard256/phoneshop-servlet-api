package com.es.phoneshop.web;

import com.es.phoneshop.DAO.Product.ArrayListProductDao;
import com.es.phoneshop.DAO.Product.ProductDao;
import com.es.phoneshop.entity.product.Product;
import com.es.phoneshop.exception.InvalidParamException;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class AdvancedSearchServlet extends HttpServlet {

    protected static final String ADVANCED_SEARCH_JSP = "/WEB-INF/pages/advancedSearch.jsp";
    private ProductDao productDao;

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        productDao = ArrayListProductDao.getInstance();
    }

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String query = request.getParameter("query");
        String minPrice = request.getParameter("minPrice");
        String maxPrice = request.getParameter("maxPrice");

        String searchOption = request.getParameter("searchOption");

        List<Product> products = new ArrayList<>();

        try {
            products = productDao.advancedSearch(query, minPrice, maxPrice,searchOption);
        } catch (InvalidParamException e) {
            if (e.code == InvalidParamException.codes.MIN) {
                request.setAttribute("error", e.toString());
            } else {
                request.setAttribute("error", e.toString());
            }
        }

        request.setAttribute("products", products);
        request.getRequestDispatcher(ADVANCED_SEARCH_JSP).forward(request, response);
    }
}