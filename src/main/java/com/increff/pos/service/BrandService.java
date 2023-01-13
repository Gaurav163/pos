package com.increff.pos.service;

import com.increff.pos.dao.BrandDao;
import com.increff.pos.pojo.BrandPojo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Locale;

@Service
public class BrandService {
    private final BrandDao brandDao;

    @Autowired
    public BrandService(BrandDao brandDao) {
        this.brandDao = brandDao;
    }

    public BrandPojo save(BrandPojo brand) {
        BrandPojo check = brandDao.findByNameAndCategory(brand.getName(), brand.getCategory());
        normalize(brand);
        if (brand.getName().isEmpty() || brand.getCategory().isEmpty()) {
            throw new IllegalStateException("Brand Name and Category are required to create Brand.");
        }
        if (check != null) {
            throw new IllegalStateException("Brand Already exist with given Name and Category");
        }
        brand.setId(null);
        return brandDao.save(brand);
    }

    public BrandPojo getById(Long id) {
        BrandPojo brand = brandDao.findById(id);
        if (brand == null) {
            throw new IllegalStateException("Brand not found");
        }
        return brand;
    }

    public List<BrandPojo> getByName(String name) {
        return brandDao.findByName(name);
    }

    public List<BrandPojo> getAll() {
        return brandDao.findAll();
    }

    public List<BrandPojo> getByCategory(String category) {
        return brandDao.findByCategory(category);
    }

    public BrandPojo getByName(String name, String category) {
        return brandDao.findByNameAndCategory(name, category);
    }

    @Transactional
    public BrandPojo update(Long id, BrandPojo updated) {
        normalize(updated);
        BrandPojo brand = brandDao.findById(id);
        if (brand == null) {
            throw new IllegalStateException("Brand with ID :" + id.toString() + " does not exist.");
        }
        if (updated.getCategory() != null && updated.getCategory().length() >= 1) {
            brand.setCategory(updated.getCategory());
        }
        if (updated.getName() != null && updated.getName().length() >= 1) {
            brand.setName(updated.getName());
        }
        return brand;
    }

    public static void normalize(BrandPojo b) {
        b.setName(b.getName().toLowerCase().trim());
        b.setCategory(b.getCategory().toLowerCase().trim());
    }
}
