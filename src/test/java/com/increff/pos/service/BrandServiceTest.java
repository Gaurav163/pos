package com.increff.pos.service;

import com.increff.pos.pojo.BrandPojo;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static org.junit.Assert.*;

public class BrandServiceTest extends AbstractUnitTest {

    @Autowired
    private BrandService brandService;

    @Test
    public void testNormalize() {
        BrandPojo b = new BrandPojo("  Name", "cATEGory ");
        brandService.normalize(b);
        BrandPojo test = new BrandPojo("name", "category");
        assertEquals(b, test);

    }

    @Test(expected = IllegalStateException.class)
    public void testSave() {
        BrandPojo b = new BrandPojo();
        b.setName("NamE");
        b.setCategory("CatEGORY");
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
    public void testAllGet() {
        List<BrandPojo> list = brandService.getAll();
        assertFalse(list.isEmpty());
    }
}
