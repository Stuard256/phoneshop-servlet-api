package com.es.phoneshop.service.cart;

import com.es.phoneshop.DAO.Product.ArrayListProductDao;
import com.es.phoneshop.DAO.Product.ProductDao;
import com.es.phoneshop.entity.cart.Cart;
import com.es.phoneshop.entity.cart.CartItem;
import com.es.phoneshop.entity.product.Product;
import com.es.phoneshop.exception.OutOfStockException;
import com.es.phoneshop.exception.ProductNotFoundException;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Optional;

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
        Optional<CartItem> found = cart.getItems().stream().filter(item -> item.getProduct().getId().equals(cartItem.getProduct().getId())).findAny();
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
            cart.setItems(new ArrayList<>());
        }
        return cart;
    }

    @Override
    public synchronized void add(Cart cart, Long productId, int quantity) throws
            OutOfStockException, ProductNotFoundException {
        Product product = products.getProduct(productId);
        if (product == null) {
            throw new ProductNotFoundException();
        }
        checkQuantity(cart, product, quantity);
        addItem(cart, new CartItem(product, quantity));
        recalculateCart(cart);
    }

    @Override
    public synchronized void update(Cart cart, Long productId, int quantity) throws
            OutOfStockException, ProductNotFoundException {
        Product product = products.getProduct(productId);
        if (product == null) {
            throw new ProductNotFoundException();
        }
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

    @Override
    public void clear(Cart cart) {
        cart.getItems().clear();
        cart.setTotalCost(null);
        cart.setTotalQuantity(0);
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
        cart.getItems().forEach(item -> totalCost[0] = totalCost[0].add(item.getProduct().getPrice().multiply(BigDecimal.valueOf(item.getQuantity()))));
        cart.setTotalCost(totalCost[0]);
    }

    private static class SingletonHelper {
        private static final DefaultCartService INSTANCE = new DefaultCartService();
    }

}
