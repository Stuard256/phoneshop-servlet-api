package com.es.phoneshop.web.product;

import com.es.phoneshop.DAO.Product.ArrayListProductDao;
import com.es.phoneshop.DAO.Product.ProductDao;
import com.es.phoneshop.entity.product.Product;
import com.es.phoneshop.exception.OutOfStockException;
import com.es.phoneshop.exception.ProductNotFoundException;
import com.es.phoneshop.service.cart.CartService;
import com.es.phoneshop.service.cart.DefaultCartService;
import com.es.phoneshop.service.product.DefaultLastSeenProductsService;
import com.es.phoneshop.service.product.LastSeenProductsService;
import com.es.phoneshop.web.ErrorHandler;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class ProductDetailsPageServlet extends HttpServlet {
    private ProductDao productDao;
    private CartService cartService;
    private LastSeenProductsService lastSeen;

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        productDao = ArrayListProductDao.getInstance();
        cartService = DefaultCartService.getInstance();
        lastSeen = DefaultLastSeenProductsService.getInstance();

    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            Long productId = parseProductId(request);
            Product product = productDao.getProduct(productId);
            if (product == null) {
                throw new ProductNotFoundException();
            }
            request.setAttribute("product", productDao.getProduct(productId));
            request.setAttribute("cart", cartService.getCart(request));
            lastSeen.addToLastSeen(request, product);
            request.setAttribute("lastSeen", lastSeen.getLastSeen(request));
            request.getRequestDispatcher("/WEB-INF/pages/product.jsp").forward(request, response);
        } catch (NumberFormatException | ProductNotFoundException e) {
            ErrorHandler.handle(request, response, e);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Long productId = parseProductId(request);
        String quantityString = request.getParameter("quantity");
        int quantity;
        try {
            quantity = Integer.parseInt(quantityString);
            cartService.add(cartService.getCart(request), productId, quantity);
        } catch (NumberFormatException e) {
            request.setAttribute("error", "Quantity must be number");
            doGet(request, response);
            return;
        } catch (ProductNotFoundException e) {
            ErrorHandler.handle(request, response, e);
            return;
        } catch (OutOfStockException e) {
            ErrorHandler.handle(request, response, e);
            doGet(request, response);
            return;
        }
        response.sendRedirect(request.getContextPath() + "/products/" + productId + "?message=Product added to cart");
    }

    private Long parseProductId(HttpServletRequest request) {
        String productInfo = request.getPathInfo().substring(1);
        return Long.parseLong(productInfo);
    }
}
