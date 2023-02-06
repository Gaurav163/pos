package com.increff.pos.service;

import com.increff.pos.dao.BrandDao;
import com.increff.pos.model.ApiException;
import com.increff.pos.pojo.Brand;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(rollbackFor = Exception.class)
public class BrandService {
    @Autowired
    private BrandDao brandDao;

    public Brand getById(Long id) {
        return brandDao.getById(id);
    }

    public List<Brand> getAll() {
        return brandDao.getAll();
    }

    public List<Brand> getListByParameter(String name, String value) {
        return brandDao.getListByMember(name, value);
    }

    public Brand getByNameAndCategory(String name, String category) {
        return brandDao.getByNameAndCategory(name, category);
    }

    public Brand create(Brand brand) throws ApiException {
        Brand existingBrand = brandDao.getByNameAndCategory(brand.getName(), brand.getCategory());
        if (existingBrand != null) {
            throw new ApiException("Brand already exist with provided name and category");
        }
        brandDao.create(brand);
        return brand;
    }

    public Brand update(Long id, Brand newBrand) throws ApiException {

        Brand brand = brandDao.getById(id);
        if (brand == null) {
            throw new ApiException("Brand with ID :" + id.toString() + " does not exist");
        }
        if (newBrand.getName() == null || newBrand.getName().isEmpty()) {
            newBrand.setName(brand.getName());
        }
        if (newBrand.getCategory() == null || newBrand.getCategory().isEmpty()) {
            newBrand.setCategory(brand.getCategory());
        }
        Brand existingBrand = brandDao.getByNameAndCategory(newBrand.getName(), newBrand.getCategory());
        if (existingBrand != null && !existingBrand.getId().equals(id)) {
            throw new ApiException("Brand already exist with provided name and category");
        }
        brand.setName(newBrand.getName());
        brand.setCategory(newBrand.getCategory());
        return brand;
    }
}