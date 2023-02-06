package com.increff.pos.dao;

import com.increff.pos.pojo.Product;
import org.springframework.stereotype.Repository;

@Repository
public class ProductDao extends AbstractDao<Product> {
    public ProductDao() {
        super(Product.class);
    }
}
