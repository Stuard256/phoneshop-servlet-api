package com.es.phoneshop.model.product.service;

import com.es.phoneshop.model.product.DAO.ArrayListProductDao;
import com.es.phoneshop.model.product.DAO.ProductDao;
import com.es.phoneshop.model.product.entity.Cart;
import com.es.phoneshop.model.product.entity.CartItem;
import com.es.phoneshop.model.product.entity.Product;
import com.es.phoneshop.model.product.exception.OutOfStockException;

import javax.servlet.http.HttpServletRequest;

public class DefaultCartService implements CartService {
    private static final String CART_SESSION_ATTRIBUTE = DefaultCartService.class.getName() + ".cart";

    private final ProductDao products;

    private DefaultCartService() {
        products = ArrayListProductDao.getInstance();
    }

    public static DefaultCartService getInstance() {
        return SingletonHelper.INSTANCE;
    }

    @Override
    public synchronized Cart getCart(HttpServletRequest request) {
        Cart cart = (Cart) request.getSession().getAttribute(CART_SESSION_ATTRIBUTE);
        if (cart == null) {
            request.getSession().setAttribute(CART_SESSION_ATTRIBUTE, cart = new Cart());
        }
        return cart;
    }

    @Override
    public synchronized void add(Cart cart, Long productId, int quantity) throws OutOfStockException {
        Product product = products.getProduct(productId);
        CartItem sameItem = cart.getItem(product);
        if (sameItem != null) {
            if (product.getStock() < quantity + sameItem.getQuantity()) {
                throw new OutOfStockException(product, quantity + sameItem.getQuantity(), product.getStock());
            }
        } else if (product.getStock() < quantity) {
            throw new OutOfStockException(product, quantity, product.getStock());
        }
        cart.addItem(new CartItem(product, quantity));
    }

    private static class SingletonHelper {
        private static final DefaultCartService INSTANCE = new DefaultCartService();
    }
}
