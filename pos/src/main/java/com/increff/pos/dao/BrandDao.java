package com.increff.pos.dao;

import com.increff.pos.pojo.Brand;
import org.springframework.stereotype.Repository;

import javax.persistence.TypedQuery;

@Repository
public class BrandDao extends AbstractDao<Brand> {

    public BrandDao() {
        super(Brand.class);
    }

    private static final String SELECT_BY_NAME_AND_CATEGORY = "select b from Brand b where name=:name and category=:category";

    public Brand getByNameAndCategory(String name, String category) {
        TypedQuery<Brand> query = getQuery(SELECT_BY_NAME_AND_CATEGORY);
        query.setParameter("name", name);
        query.setParameter("category", category);
        return getSingle(query);
    }
}
