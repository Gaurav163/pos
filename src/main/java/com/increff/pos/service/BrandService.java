package com.increff.pos.service;

import com.increff.pos.dao.BrandDao;
import com.increff.pos.model.ApiException;
import com.increff.pos.pojo.BrandPojo;
import lombok.extern.log4j.Log4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Log4j
@Service
public class BrandService extends AbstractService<BrandPojo> {
    private final BrandDao brandDao;

    @Autowired
    public BrandService(BrandDao brandDao) {
        this.brandDao = brandDao;
        this.dao = brandDao;
    }

    // TODO change to spring transaction
    @Transactional(rollbackOn = ApiException.class)
    public BrandPojo create(BrandPojo brand) throws ApiException {
        BrandPojo check = brandDao.findByNameAndCategory(brand.getName(), brand.getCategory());
        if (check != null) {
            throw new ApiException("Brand Already exist with given Name and Category");
        }
        brandDao.create(brand);
        return brand;
    }


    public BrandPojo getByNameAndCategory(String name, String category) {
        return brandDao.findByNameAndCategory(name, category);
    }

    @Transactional(rollbackOn = ApiException.class)
    public BrandPojo update(Long id, BrandPojo form) throws ApiException {
        BrandPojo brand = brandDao.findById(id);
        if (brand == null) {
            log.error("wrong Id");
            throw new ApiException("Brand with ID :" + id.toString() + " does not exist.");
        }
        BrandPojo check = brandDao.findByNameAndCategory(form.getName(), form.getCategory());
        if (check != null && check.getId() != id) {
            log.error("brand and category Already Exist");
            throw new ApiException("Brand with given Name and Category Already Exist.");
        }
        brand.setCategory(form.getCategory());
        brand.setName(form.getName());
        return brand;
    }
}
