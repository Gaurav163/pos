package com.increff.pos.service;

import com.increff.pos.AbstractUnitTest;
import com.increff.pos.TestHelper;
import com.increff.pos.dao.ProductDao;
import com.increff.pos.model.ApiException;
import com.increff.pos.pojo.Product;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.junit.Assert.*;

public class ProductServiceTest extends AbstractUnitTest {
    @Autowired
    private ProductService productService;
    @Autowired
    private ProductDao productDao;
    @Autowired
    private TestHelper testHelper;

    @Test
    public void testCreate() throws ApiException {
        Product product = testHelper.getProduct("product1", "barcode1", 1L, 59.9);
        productService.create(product);
        Product savedProduct = productDao.getByParameter("barcode", "barcode1");
        assertNotNull(savedProduct);
        assertEquals(product.getName(), savedProduct.getName());
    }

    @Test(expected = ApiException.class)
    public void testCreateDuplicateBarcode() throws ApiException {
        Product product = testHelper.getProduct("product1", "barcode1", 1L, 59.9);
        Product product2 = testHelper.getProduct("product2", "barcode1", 3L, 59.9);
        productService.create(product);
        productService.create(product2);
        productDao.getByParameter("barcode", "barcode1");
    }

    @Test
    public void testUpdate() throws ApiException {
        Product product = testHelper.createProduct("product1", "barcode1", 1L, 59.9);
        Product product2 = testHelper.getProduct("product1", "barcode2", 3L, 59.9);
        productService.update(product.getId(), product2);
        Product previousProduct = productDao.getByParameter("barcode", "barcode1");
        Product currentProduct = productDao.getByParameter("barcode", "barcode2");
        assertNull(previousProduct);
        assertNotNull(currentProduct);
    }

    @Test(expected = ApiException.class)
    public void testUpdateDuplicateBarcode() throws ApiException {
        Product product1 = testHelper.createProduct("product1", "barcode1", 1L, 59.9);
        Product product2 = testHelper.createProduct("product2", "barcode3", 1L, 59.9);
        Product product3 = testHelper.getProduct("product2", "barcode1", 3L, 59.9);
        productService.update(product2.getId(), product3);
        productDao.getByParameter("barcode", "barcode1");
    }

    @Test(expected = ApiException.class)
    public void testUpdateWrongId() throws ApiException {
        Product product1 = testHelper.createProduct("product1", "barcode1", 1L, 59.9);
        Product product2 = testHelper.getProduct("product2", null, null, null);
        productService.update(product1.getId() + 10, product2);
        productDao.getByParameter("barcode", "barcode1");
    }

    @Test
    public void testUpdateNameOnly() throws ApiException {
        Product product1 = testHelper.createProduct("product1", "barcode1", 1L, 59.9);
        Product product2 = testHelper.getProduct("product2", null, null, null);
        productService.update(product1.getId(), product2);
        Product currentProduct = productDao.getByParameter("barcode", "barcode1");
        assertNotNull(currentProduct);
        assertEquals(product2.getName(), currentProduct.getName());
    }


}
