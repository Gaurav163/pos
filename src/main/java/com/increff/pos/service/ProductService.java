package com.increff.pos.service;

import com.increff.pos.dao.ProductDao;
import com.increff.pos.model.ApiException;
import com.increff.pos.pojo.ProductPojo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(rollbackFor = ApiException.class)
public class ProductService {

    @Autowired
    private ProductDao productDao;

    public ProductPojo getById(Long id) {
        return productDao.getById(id);
    }

    public List<ProductPojo> getAll() {
        return productDao.getAll();
    }

    public ProductPojo getOneByParameter(String name, String value) {
        return productDao.getOneByMember(name, value);
    }

    public List<ProductPojo> getListByParameter(String name, String value) {
        return productDao.getListByMember(name, value);
    }

    public ProductPojo create(ProductPojo pojo) throws ApiException {
        if (productDao.getOneByMember("barcode", pojo.getBarcode()) != null) {
            throw new ApiException("Barcode already assigned to another product");
        }
        productDao.create(pojo);
        return pojo;
    }

    public ProductPojo update(Long id, ProductPojo form) throws ApiException {
        ProductPojo existingBarcode = productDao.getOneByMember("barcode", form.getBarcode());
        if (existingBarcode != null && !id.equals(existingBarcode.getId())) {
            throw new ApiException("Barcode already assigned to another product");
        }
        ProductPojo pojo = productDao.getById(id);
        if (pojo == null) {
            throw new ApiException("Product not found with ID: " + id);
        }
        pojo.setBarcode(form.getBarcode());
        pojo.setName(form.getName());
        pojo.setMrp(form.getMrp());
        pojo.setBrandId(form.getBrandId());
        return pojo;

    }
}
