package com.increff.pos.service;

import com.increff.pos.AbstractUnitTest;
import com.increff.pos.Helper;
import com.increff.pos.dao.BrandDao;
import com.increff.pos.model.ApiException;
import com.increff.pos.pojo.Brand;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static org.junit.Assert.*;

public class BrandServiceTest extends AbstractUnitTest {

    @Autowired
    private BrandService brandService;
    @Autowired
    private BrandDao brandDao;
    @Autowired
    private Helper helper;


    @Test
    public void testCreate() throws ApiException {
        Brand brand = helper.createBrand("brand1", "category11");
        Brand savedBrand = brandDao.getByNameAndCategory("brand1", "category11");
        assertNotNull(brand);
        assertNotNull(brand.getId());
    }

    @Test(expected = ApiException.class)
    public void testCreateDuplicate() throws ApiException {
        brandService.create(helper.getBrand("brand1", "category1"));
        brandService.create(helper.getBrand("brand1", "category1"));
        brandDao.getByParameter("name", "brand1");
    }

    @Test
    public void testGetAll() {
        helper.generateBrands(5);
        List<Brand> list = brandDao.getAll();
        assertEquals(5, list.size());
    }

    @Test
    public void testGetById() {
        Brand brand = helper.createBrand("brand1", "category1");
        Brand savedBrand1 = brandDao.getByNameAndCategory("brand1", "category1");
        Brand savedBrand = brandDao.getById(brand.getId());
        assertNotNull(savedBrand);
        assertNotNull(savedBrand1);
        assertEquals("brand1", savedBrand.getName());
        assertEquals("category1", savedBrand.getCategory());
        Brand fakeBrand = brandDao.getById(brand.getId() + 10);
        assertNull(fakeBrand);
    }


    @Test
    public void testUpdate() throws ApiException {
        Brand brand = helper.createBrand("brand1", "category1");
        Brand brand1 = helper.getBrand("brand12", "category12");

        brandService.update(brand.getId(), brand1);
        Brand updatedBrand = brandDao.getByNameAndCategory("brand12", "category12");
        Brand oldBrand = brandDao.getByNameAndCategory("brand1", "category1");
        assertNotNull(updatedBrand);
        assertNull(oldBrand);

    }

    @Test()
    public void testUpdateSingleParam() throws ApiException {
        Brand brand = helper.createBrand("brand1", "category1");
        Brand brand1 = helper.getBrand("brand12", "");
        brandService.update(brand.getId(), brand1);
        Brand brand2 = helper.getBrand(null, "category12");
        brandService.update(brand.getId(), brand2);
        Brand updatedBrand = brandDao.getByNameAndCategory("brand12", "category12");
        Brand oldBrand = brandDao.getByNameAndCategory("brand1", "category1");
        assertNotNull(updatedBrand);
        assertNull(oldBrand);
    }

    @Test(expected = ApiException.class)
    public void testUpdateDuplicate() throws ApiException {
        Brand brand = helper.createBrand("brand1", "category1");
        Brand brand2 = helper.createBrand("brand2", "category2");
        Brand brand3 = helper.getBrand("brand1", "category1");
        brandService.update(brand2.getId(), brand3);
    }

    @Test(expected = ApiException.class)
    public void testUpdateInvalidId() throws ApiException {
        Brand brand = helper.createBrand("brand1", "category1");
        Brand brand2 = helper.createBrand("brand2", "category2");
        Brand brand3 = helper.getBrand("brand1", "category1");
        brandService.update(brand2.getId() + 10, brand3);
    }


}


