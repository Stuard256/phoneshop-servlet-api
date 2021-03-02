package com.es.phoneshop.web;

import com.es.phoneshop.model.product.exception.OutOfStockException;
import com.es.phoneshop.model.product.exception.ProductNotFoundException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.ParseException;

public class ErrorHandler {
    static public void handle(HttpServletRequest request, HttpServletResponse response, Exception e) throws ServletException, IOException {
        if(e.getClass().equals(NumberFormatException.class)){
            request.setAttribute("error", "This Id: " + request.getPathInfo().substring(1) + " isn't number! ");
            request.getRequestDispatcher("/WEB-INF/pages/errorProductNotFound.jsp").forward(request, response);
        }
        if(e.getClass().equals(ProductNotFoundException.class)){
            request.setAttribute("error", e.toString());
            request.getRequestDispatcher("/WEB-INF/pages/errorProductNotFound.jsp").forward(request, response);
        }
        if(e.getClass().equals(OutOfStockException.class)){
            request.setAttribute("error", e.toString());
        }
    }
}
