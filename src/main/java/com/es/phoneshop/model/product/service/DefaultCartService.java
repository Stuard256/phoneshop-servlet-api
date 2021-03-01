package com.es.phoneshop.model.product.service;

import com.es.phoneshop.model.product.DAO.ArrayListProductDao;
import com.es.phoneshop.model.product.DAO.ProductDao;
import com.es.phoneshop.model.product.entity.Cart;
import com.es.phoneshop.model.product.entity.CartItem;
import com.es.phoneshop.model.product.entity.Product;
import com.es.phoneshop.model.product.exception.OutOfStockException;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;

public class DefaultCartService implements CartService {
    private static final String CART_SESSION_ATTRIBUTE = DefaultCartService.class.getName() + ".cart";

    private final ProductDao products;

    private DefaultCartService() {
        products = ArrayListProductDao.getInstance();
    }

    public static DefaultCartService getInstance() {
        return SingletonHelper.INSTANCE;
    }

    public Optional<CartItem> getItem(Cart cart, Long productId) {
        return cart.getItems().stream().filter(item -> item.getProduct().getId().equals(productId)).findAny();
    }

    public void addItem(Cart cart, CartItem cartItem) {
        Optional<CartItem> found = cart.getItems().stream().filter(item -> item.getProduct().equals(cartItem.getProduct())).findAny();
        if (found.isPresent()) {
            found.get().increaseQuantity(cartItem.getQuantity());
        } else {
            cart.getItems().add(cartItem);
        }
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
        checkQuantity(cart, product, quantity);
        addItem(cart, new CartItem(product, quantity));
        recalculateCart(cart);
    }

    @Override
    public synchronized void update(Cart cart, Long productId, int quantity) throws OutOfStockException {
        Product product = products.getProduct(productId);
        Optional<CartItem> item = getItem(cart, productId);
        checkQuantity(cart, product, quantity);
        if (quantity == 0) {
            delete(cart, productId);
        }
        item.ifPresent(cartItem -> cartItem.setQuantity(quantity));
        recalculateCart(cart);
    }

    @Override
    public void delete(Cart cart, Long productId) {
        cart.getItems().removeIf(item -> productId.equals(item.getProduct().getId()));
        recalculateCart(cart);
    }

    private void checkQuantity(Cart cart, Product product, int quantity) throws OutOfStockException {
        if (quantity < 0) {
            throw new OutOfStockException(product, quantity, product.getStock());
        }
        Optional<CartItem> item = getItem(cart, product.getId());
        if (item.isPresent()) {
            if (product.getStock() < quantity + item.get().getQuantity()) {
                throw new OutOfStockException(product, quantity + item.get().getQuantity(), product.getStock());
            }
        } else if (product.getStock() < quantity) {
            throw new OutOfStockException(product, quantity, product.getStock());
        }
    }

    private void recalculateCart(Cart cart) {
        cart.setTotalQuantity(
                cart.getItems().stream()
                        .mapToInt(CartItem::getQuantity)
                        .sum());
        final BigDecimal[] totalCost = {new BigDecimal(0)};
        cart.getItems().forEach(item -> {
            totalCost[0] = totalCost[0].add(item.getProduct().getPrice().multiply(BigDecimal.valueOf(item.getQuantity())));
        });
            cart.setTotalCost(totalCost[0]);
    }

    private static class SingletonHelper {
        private static final DefaultCartService INSTANCE = new DefaultCartService();
    }
}
