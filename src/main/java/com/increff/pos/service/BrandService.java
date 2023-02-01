package com.increff.pos.service;

import com.increff.pos.dao.BrandDao;
import com.increff.pos.model.ApiException;
import com.increff.pos.pojo.BrandPojo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(rollbackFor = ApiException.class)
public class BrandService {
    @Autowired
    private BrandDao brandDao;

    public BrandPojo getById(Long id) {
        return brandDao.getById(id);
    }

    public List<BrandPojo> getAll() {
        return brandDao.getAll();
    }

    public List<BrandPojo> getListByParameter(String name, String value) {
        return brandDao.getListByMember(name, value);
    }

    public BrandPojo getByNameAndCategory(String name, String category) {
        return brandDao.getByNameAndCategory(name, category);
    }

    public BrandPojo create(BrandPojo brand) throws ApiException {
        BrandPojo existingBrand = brandDao.getByNameAndCategory(brand.getName(), brand.getCategory());
        if (existingBrand != null) {
            throw new ApiException("Brand already exist with provided name and category");
        }
        brandDao.create(brand);
        return brand;
    }

    public BrandPojo update(Long id, BrandPojo newPojo) throws ApiException {

        BrandPojo brand = brandDao.getById(id);
        if (brand == null) {
            throw new ApiException("Brand with ID :" + id.toString() + " does not exist");
        }
        if (newPojo.getName() == null || newPojo.getName().isEmpty()) {
            newPojo.setName(brand.getName());
        }
        if (newPojo.getCategory() == null || newPojo.getCategory().isEmpty()) {
            newPojo.setCategory(brand.getCategory());
        }
        BrandPojo existingPojo = brandDao.getByNameAndCategory(newPojo.getName(), newPojo.getCategory());
        if (existingPojo != null && !existingPojo.getId().equals(id)) {
            throw new ApiException("Brand already exist with provided name and category");
        }
        brand.setName(newPojo.getName());
        brand.setCategory(newPojo.getCategory());
        return brand;
    }
}
