package com.increff.pos.helper;

import com.increff.pos.dao.BrandDao;
import com.increff.pos.model.BrandForm;
import com.increff.pos.pojo.Brand;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class BrandHelper {
    @Autowired
    private BrandDao brandDao;

    public Brand getBrand(String name, String category) {
        Brand brand = new Brand();
        brand.setName(name);
        brand.setCategory(category);
        return brand;
    }

    public BrandForm getBrandForm(String name, String category) {
        BrandForm brandForm = new BrandForm();
        brandForm.setName(name);
        brandForm.setCategory(category);
        return brandForm;
    }

    public Brand createBrand(String name, String category) {
        return brandDao.create(getBrand(name, category));
    }

    public void generateBrands(int count) {
        for (int i = 1; i <= count; i++) {
            createBrand("brand" + i, "category" + i);
        }
    }
}
