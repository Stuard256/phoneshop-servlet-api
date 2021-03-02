package com.es.phoneshop.model.product;

import com.es.phoneshop.DAO.Product.ArrayListProductDao;
import com.es.phoneshop.DAO.Product.ProductDao;
import com.es.phoneshop.entity.product.Product;
import com.es.phoneshop.exception.ProductNotFoundException;
import org.junit.Before;
import org.junit.Test;

import java.math.BigDecimal;
import java.util.Currency;

import static org.junit.Assert.*;

public class ArrayListProductDaoTest
{
    private ProductDao productDao;

    @Before
    public void setup() {
        productDao = ArrayListProductDao.getInstance();
        productDao.findProducts("",null,null)
                .forEach(p -> productDao.delete(p.getId()));
    }

    @Test
    public void testFindProductsNoResults() {
        assertTrue(productDao.findProducts().isEmpty());
    }

    @Test
    public void testSaveNewProduct() throws ProductNotFoundException {
        Currency usd = Currency.getInstance("USD");
        Product product = new Product( "test-product", "TEST", new BigDecimal(10), usd, 15, "https://raw.githubusercontent.com/andrewosipenko/phoneshop-ext-images/master/manufacturer/Siemens/Siemens%20SXG75.jpg");
        productDao.save(product);

        assertNotNull(product.getId());
        Product result = productDao.getProduct(product.getId());
        assertNotNull(result);
        assertEquals("test-product",result.getCode());
    }

    @Test
    public void testDeleteProduct() throws ProductNotFoundException {
        Currency usd = Currency.getInstance("USD");
        Product product = new Product( "test-product", "TEST", new BigDecimal(400), usd, 190, "https://raw.githubusercontent.com/andrewosipenko/phoneshop-ext-images/master/manufacturer/Siemens/Siemens%20SXG75.jpg");
        productDao.save(product);
        productDao.delete(product.getId());
        assertNull(productDao.getProduct(product.getId()));
    }

    @Test
    public void testFindProductWithZeroStock(){
        Currency usd = Currency.getInstance("USD");
        Product product = new Product( "test-product", "TEST", new BigDecimal(10), usd, 0, "https://raw.githubusercontent.com/andrewosipenko/phoneshop-ext-images/master/manufacturer/Siemens/Siemens%20SXG75.jpg");
        productDao.save(product);
        assertNull(product.getId());
    }

    @Test
    public void testFindProductWithNullPrice(){
        Currency usd = Currency.getInstance("USD");
        Product product = new Product( "test-product", "TEST", null, usd, 190, "https://raw.githubusercontent.com/andrewosipenko/phoneshop-ext-images/master/manufacturer/Siemens/Siemens%20SXG75.jpg");
        productDao.save(product);
        assertNull(product.getId());
    }

    @Test
    public void testFindProductsWithEmptyQuery() {
        Currency usd = Currency.getInstance("USD");
        Product product = new Product( "test-product", "TEST", new BigDecimal(400), usd, 190, "https://raw.githubusercontent.com/andrewosipenko/phoneshop-ext-images/master/manufacturer/Siemens/Siemens%20SXG75.jpg");
        productDao.save(product);
        assertArrayEquals(productDao.findProducts().toArray(), productDao.findProducts("").toArray());
    }

    @Test
    public void testReplaceProduct() throws ProductNotFoundException {
        Currency usd = Currency.getInstance("USD");
        Product product = new Product( 2L,"test-product-for-replace", "TEST", new BigDecimal(10), usd, 15, "https://raw.githubusercontent.com/andrewosipenko/phoneshop-ext-images/master/manufacturer/Siemens/Siemens%20SXG75.jpg");
        productDao.save(product);

        assertNotNull(product.getId());
        Product result = productDao.getProduct(product.getId());
        assertNotNull(result);
        assertEquals("test-product-for-replace",result.getCode());
    }

    @Test
    public void testFindProductContainsRightResult(){
        Currency usd = Currency.getInstance("USD");
        Product product1 = new Product( "test-product", "TEST", new BigDecimal(400), usd, 190, "https://raw.githubusercontent.com/andrewosipenko/phoneshop-ext-images/master/manufacturer/Siemens/Siemens%20SXG75.jpg");
        Product product2 = new Product( "test-product2", "TESTED", new BigDecimal(400), usd, 190, "https://raw.githubusercontent.com/andrewosipenko/phoneshop-ext-images/master/manufacturer/Siemens/Siemens%20SXG75.jpg");
        productDao.save(product1);
        productDao.save(product2);
        assertTrue(productDao.findProducts(product1.getDescription()).contains(product1));
    }

}
