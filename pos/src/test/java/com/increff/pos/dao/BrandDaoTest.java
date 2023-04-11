package com.increff.pos.dao;

import com.increff.pos.AbstractUnitTest;
import com.increff.pos.TestHelper;
import com.increff.pos.pojo.Brand;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import javax.persistence.PersistenceException;
import java.util.List;

import static org.junit.Assert.*;

public class BrandDaoTest extends AbstractUnitTest {

    @Autowired
    private BrandDao brandDao;
    @Autowired
    private TestHelper testHelper;

    @Test
    public void testCreate() {
        Brand brand = testHelper.createBrand("brand", "category");
        Brand savedBrand = brandDao.getByParameter("name", "brand");
        assertNotNull(savedBrand);
        assertEquals(brand.getName(), savedBrand.getName());
        assertEquals(brand.getCategory(), savedBrand.getCategory());
    }

    @Test(expected = PersistenceException.class)
    public void testCreateDuplicate() {
        Brand brand = testHelper.createBrand("brand", "category");
        Brand brand2 = testHelper.createBrand("brand", "category");
        brandDao.em.flush();
    }

    @Test
    public void testGetAll() {
        Brand brand1 = testHelper.createBrand("brand1", "category1");
        Brand brand2 = testHelper.createBrand("brand2", "category2");
        List<Brand> brandList = brandDao.getAll();
        assertEquals(2, brandList.size());
    }

    @Test
    public void testGetById() {
        Brand brand1 = testHelper.createBrand("brand1", "category1");
        Brand checkBrand = brandDao.getById(brand1.getId());
        assertNotNull(checkBrand);
    }

    @Test
    public void testGetByInvalidId() {
        Brand brand1 = testHelper.createBrand("brand1", "category1");
        Brand checkBrand = brandDao.getById(brand1.getId() + 10L);
        assertNull(checkBrand);
    }

    @Test
    public void testGetListByParameter() {
        testHelper.createBrand("brand1", "category1");
        testHelper.createBrand("brand2", "category1");
        testHelper.createBrand("brand1", "category2");
        testHelper.createBrand("brand2", "category2");
        List<Brand> listByName = brandDao.getListByParameter("name", "brand1");
        List<Brand> listByCategory = brandDao.getListByParameter("category", "category1");
        assertEquals(2, listByName.size());
        assertEquals(2, listByCategory.size());
    }

    @Test
    public void testGetByNameAndCategory() {
        Brand brand1 = testHelper.createBrand("brand1", "category1");
        Brand brand2 = testHelper.createBrand("brand2", "category2");
        Brand checkBrand1 = brandDao.getByNameAndCategory("brand1", "category1");
        Brand checkBrand2 = brandDao.getByNameAndCategory("brand1", "category2");
        assertNotNull(checkBrand1);
        assertNull(checkBrand2);
    }


}
