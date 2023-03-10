package com.increff.pos.service;

import com.increff.pos.dao.BrandDao;
import com.increff.pos.model.ApiException;
import com.increff.pos.pojo.Brand;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

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
        return brandDao.getListByParameter(name, value);
    }

    public Brand getByNameAndCategory(String name, String category) {
        return brandDao.getByNameAndCategory(name, category);
    }

    public Map<Long, Brand> getListByIdList(List<Long> idList) {
        if (idList.isEmpty()) {
            return new HashMap<>();
        } else {
            return brandDao.getListByIdList(idList).stream().collect(Collectors.toMap(Brand::getId, Function.identity()));
        }
    }

    public Brand create(Brand brand) throws ApiException {
        Brand existingBrand = brandDao.getByNameAndCategory(brand.getName(), brand.getCategory());
        if (existingBrand != null) {
            throw new ApiException("Brand category pair already exist");
        }
        brandDao.create(brand);
        return brand;
    }

    public Brand update(Long id, Brand newBrand) throws ApiException {
        Brand brand = brandDao.getById(id);
        if (brand == null) {
            throw new ApiException("Invalid brand ID");
        }
        Brand checkBrand = new Brand();
        if (newBrand.getName() != null && !newBrand.getName().isEmpty()) {
            checkBrand.setName(newBrand.getName());
        } else {
            checkBrand.setName(brand.getName());
        }
        if (newBrand.getCategory() != null && !newBrand.getCategory().isEmpty()) {
            checkBrand.setCategory(newBrand.getCategory());
        } else {
            checkBrand.setCategory(brand.getCategory());
        }
        Brand existingBrand = brandDao.getByNameAndCategory(checkBrand.getName(), checkBrand.getCategory());
        if (existingBrand != null && !existingBrand.getId().equals(id)) {
            throw new ApiException("Brand category pair already exist");
        }
        brand.setName(checkBrand.getName());
        brand.setCategory(checkBrand.getCategory());
        return brand;
    }
}
