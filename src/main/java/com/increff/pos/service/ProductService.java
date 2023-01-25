package com.increff.pos.service;

import com.increff.pos.dao.ProductDao;
import com.increff.pos.model.ApiException;
import com.increff.pos.pojo.ProductPojo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
public class ProductService extends AbstractService<ProductPojo> {

    private final ProductDao productDao;

    @Autowired
    public ProductService(ProductDao productDao) {
        this.productDao = productDao;
        this.dao = productDao;
    }

    @Transactional(rollbackOn = ApiException.class)
    public ProductPojo create(ProductPojo pojo) throws ApiException {
        if (productDao.findOneByMember("barcode", pojo.getBarcode()) != null) {
            throw new ApiException("Barcode Already Assigned to other Product");
        }
        productDao.create(pojo);
        return pojo;
    }

    @Transactional(rollbackOn = ApiException.class)
    public ProductPojo update(Long id, ProductPojo form) throws ApiException {
        ProductPojo checkBarcode = productDao.findOneByMember("barcode", form.getBarcode());
        if (checkBarcode != null && !id.equals(checkBarcode.getId())) {
            throw new ApiException("Barcode Already Assigned to other Product");
        }
        ProductPojo pojo = productDao.findById(id);
        if (pojo == null) {
            throw new ApiException("Product not found");
        }
        pojo.setBarcode(form.getBarcode());
        pojo.setName(form.getName());
        pojo.setMrp(form.getMrp());
        pojo.setBrandId(form.getBrandId());
        return pojo;

    }
}
