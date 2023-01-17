package com.increff.pos.service;

import com.increff.pos.dao.BrandDao;
import com.increff.pos.pojo.BrandPojo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
public class BrandService {
    private final BrandDao brandDao;

    @Autowired
    public BrandService(BrandDao brandDao) {
        this.brandDao = brandDao;
    }


    public static String normalize(String s) {
        return s.toLowerCase().trim();
    }

    public BrandPojo save(BrandPojo brand) {
        BrandPojo check = brandDao.findByNameAndCategory(brand.getName(), brand.getCategory());
        if (check != null) {
            throw new IllegalStateException("Brand Already exist with given Name and Category");
        }
        brandDao.save(brand);
        return brand;
    }

    public BrandPojo getById(Long id) {
        BrandPojo brand = brandDao.findById(id);

        return brand;
    }

    public List<BrandPojo> getAll() {
        return brandDao.findAll();
    }

    public List<BrandPojo> getByName(String name) {
        name = normalize(name);
        return brandDao.findByName(name);
    }


    public List<BrandPojo> getByCategory(String category) {
        category = normalize(category);
        return brandDao.findByCategory(category);
    }

    public BrandPojo getByNameAndCategory(String name, String category) {
        name = normalize(name);
        category = normalize(category);
        return brandDao.findByNameAndCategory(name, category);
    }

    @Transactional
    public BrandPojo update(Long id, BrandPojo form) {
        BrandPojo brand = brandDao.findById(id);
        if (brand == null) {
            throw new IllegalStateException("Brand with ID :" + id.toString() + " does not exist.");
        }
        BrandPojo check = brandDao.findByNameAndCategory(form.getName(), form.getCategory());
        if (check != null) {
            throw new IllegalStateException("Brand with given Name and Category Already Exist.");
        }
        brand.setCategory(form.getCategory());
        brand.setName(form.getName());
        return brand;
    }
}
