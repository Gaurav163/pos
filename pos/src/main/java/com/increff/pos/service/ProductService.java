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
        return productDao.getByParameter(name, value);
    }

    public List<Product> getListByParameter(String name, String value) {
        return productDao.getListByParameter(name, value);
    }

    public Product create(Product product) throws ApiException {
        if (productDao.getByParameter("barcode", product.getBarcode()) != null) {
            throw new ApiException("Barcode already used");
        }
        productDao.create(product);
        return product;
    }

    public Product update(Long id, Product newProduct) throws ApiException {
        Product product = productDao.getById(id);
        if (product == null) {
            throw new ApiException("Invalid product ID");
        }
        if (newProduct.getBarcode() != null && !newProduct.getBarcode().isEmpty()) {
            Product existingProduct = productDao.getByParameter("barcode", newProduct.getBarcode());
            if (existingProduct != null && !product.getId().equals(existingProduct.getId())) {
                throw new ApiException("Barcode already used");
            }
            product.setBarcode(newProduct.getBarcode());
        }
        if (newProduct.getName() != null && !newProduct.getName().isEmpty()) {
            product.setName(newProduct.getName());
        }
        if (newProduct.getMrp() != null && newProduct.getMrp() > 0) {
            product.setMrp(newProduct.getMrp());
        }
        if (newProduct.getBrandId() != null) {
            product.setBrandId(newProduct.getBrandId());
        }
        return product;

    }
}
