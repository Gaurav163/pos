package com.increff.pos.service;

import com.increff.pos.dao.ProductDao;
import com.increff.pos.model.ApiException;
import com.increff.pos.pojo.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(rollbackFor = ApiException.class)
public class ProductService {

    @Autowired
    private ProductDao productDao;

    public Product getById(Long id) {
        return productDao.getById(id);
    }

    public List<Product> getAll() {
        return productDao.getAll();
    }

    public Product getOneByParameter(String name, String value) {
        return productDao.getOneByMember(name, value);
    }

    public List<Product> getListByParameter(String name, String value) {
        return productDao.getListByMember(name, value);
    }

    public Product create(Product product) throws ApiException {
        if (productDao.getOneByMember("barcode", product.getBarcode()) != null) {
            throw new ApiException("Barcode already assigned to another product");
        }
        productDao.create(product);
        return product;
    }

    public Product update(Long id, Product form) throws ApiException {
        Product existingBarcode = productDao.getOneByMember("barcode", form.getBarcode());
        if (existingBarcode != null && !id.equals(existingBarcode.getId())) {
            throw new ApiException("Barcode already assigned to another product");
        }
        Product product = productDao.getById(id);
        if (product == null) {
            throw new ApiException("Product not found with ID: " + id);
        }
        product.setBarcode(form.getBarcode());
        product.setName(form.getName());
        product.setMrp(form.getMrp());
        product.setBrandId(form.getBrandId());
        return product;

    }
}
