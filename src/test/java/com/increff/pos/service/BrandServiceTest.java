package com.increff.pos.service;

import com.increff.pos.pojo.BrandPojo;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static org.junit.Assert.*;

public class BrandServiceTest extends AbstractUnitTest {

    @Autowired
    private BrandService brandService;


    @Test(expected = IllegalStateException.class)
    public void testSave() {
        BrandPojo b = new BrandPojo();
        b.setName("name");
        b.setCategory("category");
        brandService.save(b);
        BrandPojo expected = new BrandPojo("name", "category");
        assertEquals(b.getCategory(), expected.getCategory());
        assertEquals(b.getName(), expected.getName());
        assertNotNull(b.getId());

        // check duplicate brand
        BrandPojo check = new BrandPojo();
        check.setName(b.getName());
        check.setCategory(b.getCategory());
        brandService.save(check);
    }

    @Test
    public void testAll() {
        BrandPojo form1 = new BrandPojo("name1", "cat1");
        BrandPojo form2 = new BrandPojo("name1", "cat2");
        BrandPojo form3 = new BrandPojo("name2", "cat1");
        BrandPojo form4 = new BrandPojo("name2", "cat2");
        brandService.save(form1);
        brandService.save(form2);
        brandService.save(form3);
        brandService.save(form4);
        // Get All
        List<BrandPojo> brands = brandService.getAll();
        assertEquals(4, brands.size());
        // Get By Name
        brands = brandService.getByName("name1");
        assertEquals(2, brands.size());
        // Get By category
        brands = brandService.getByCategory("cat1");
        assertEquals(2, brands.size());
        // Get By Name and Category
        BrandPojo check = brandService.getByNameAndCategory("check", "check");
        assertNull(check);

        // check update
        BrandPojo brand = new BrandPojo("check", "check");
        System.out.println(brand);
        brand = brandService.update(form1.getId(), brand);
        System.out.println(brand);

        check = brandService.getByNameAndCategory("check", "check");
        assertNotNull(check);

        // Get by id
        brand = brandService.getById(form1.getId());
        assertNotNull(brand);
        brand = brandService.getById(20L);
        assertNull(brand);
    }


}


