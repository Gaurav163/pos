package com.increff.pos.dao;


import com.increff.pos.AbstractUnitTest;
import com.increff.pos.TestHelper;
import com.increff.pos.pojo.Product;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import javax.persistence.PersistenceException;
import java.util.List;

import static org.junit.Assert.*;

public class ProductDaoTest extends AbstractUnitTest {
    @Autowired
    private ProductDao productDao;
    @Autowired
    private TestHelper testHelper;

    @Test
    public void testCreate() {
        Product product = testHelper.createProduct("product1", "barcode1", 1L, 40.50);
        Product savedProduct = productDao.getByParameter("barcode", "barcode1");
        assertNotNull(savedProduct);
        assertEquals(product.getName(), savedProduct.getName());
        assertEquals(product.getId(), savedProduct.getId());
        assertEquals(product.getBrandId(), savedProduct.getBrandId());
        assertEquals(product.getMrp(), savedProduct.getMrp());
    }

    @Test(expected = PersistenceException.class)
    public void testCreateDuplicateBarcode() {
        testHelper.createProduct("product1", "barcode1", 1L, 40.50);
        testHelper.createProduct("product1", "barcode1", 4L, 79.90);
        productDao.em.flush();
    }

    @Test
    public void testGetAll() {
        testHelper.generateProducts(4);
        List<Product> productList = productDao.getAll();
        assertEquals(4, productList.size());
    }

    @Test
    public void testGetById() {
        Product product1 = testHelper.createProduct("product1", "barcode1", 1L, 40.50);
        Product checkProduct = productDao.getById(product1.getId());
        assertNotNull(checkProduct);
    }

    @Test
    public void testGetByInvalidId() {
        Product product1 = testHelper.createProduct("product1", "barcode1", 1L, 40.50);
        Product checkProduct = productDao.getById(product1.getId() + 4);
        assertNull(checkProduct);
    }

    @Test
    public void testGetListByParameter() {
        testHelper.createProduct("product1", "barcode1", 1L, 40.50);
        testHelper.createProduct("product2", "barcode2", 1L, 50.50);
        testHelper.createProduct("product3", "barcode3", 1L, 40.50);
        testHelper.createProduct("product4", "barcode4", 3L, 70.50);
        testHelper.createProduct("product2", "barcode5", 3L, 70.50);
        testHelper.createProduct("product3", "barcode6", 3L, 90.50);
        List<Product> listByName = productDao.getListByParameter("name", "product3");
        List<Product> listByBrandId = productDao.getListByParameter("brandId", 3L);
        assertEquals(2, listByName.size());
        assertEquals(3, listByBrandId.size());
    }

    @Test
    public void testGetByParameter() {
        testHelper.createProduct("product1", "barcode1", 1L, 40.50);
        testHelper.createProduct("product2", "barcode2", 1L, 50.50);
        Product product1 = productDao.getByParameter("barcode", "barcode1");
        Product product2 = productDao.getByParameter("barcode", "barcode4");
        assertNotNull(product1);
        assertNull(product2);
    }
}
