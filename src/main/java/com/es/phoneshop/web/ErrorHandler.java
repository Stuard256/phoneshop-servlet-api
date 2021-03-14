package com.es.phoneshop.web;

import com.es.phoneshop.exception.OutOfStockException;
import com.es.phoneshop.exception.ProductNotFoundException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class ErrorHandler {
    static public void handle(HttpServletRequest request, HttpServletResponse response, Exception e) throws ServletException, IOException {
        if(e instanceof NumberFormatException){
            request.setAttribute("error", "This Id: " + request.getPathInfo().substring(1) + " isn't number! ");
            request.getRequestDispatcher("/WEB-INF/pages/errorProductNotFound.jsp").forward(request, response);
        }else
        if(e instanceof ProductNotFoundException){
            request.setAttribute("error", e.toString());
            request.getRequestDispatcher("/WEB-INF/pages/errorProductNotFound.jsp").forward(request, response);
        }else
        if(e instanceof OutOfStockException){
            request.setAttribute("error", e.toString());
        }
    }
}
