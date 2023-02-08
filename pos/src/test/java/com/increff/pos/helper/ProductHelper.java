package com.increff.pos.helper;

import com.increff.pos.dao.ProductDao;
import com.increff.pos.pojo.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ProductHelper {
    @Autowired
    private ProductDao productDao;

    public Product getProduct(String name, String barcode, Long brandId, Double mrp) {
        Product product = new Product();
        product.setName(name);
        product.setBarcode(barcode);
        product.setBrandId(brandId);
        product.setMrp(mrp);
        return product;
    }

    public Product createProduct(String name, String barcode, Long brandId, Double mrp) {
        return productDao.create(getProduct(name, barcode, brandId, mrp));
    }

    public void generateProducts(int count) {
        for (int i = 1; i <= count; i++) {
            createProduct("name" + i, "barcode" + i, (long) i, i * 2.3);
        }
    }
}
