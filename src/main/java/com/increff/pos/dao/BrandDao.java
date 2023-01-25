package com.increff.pos.dao;

import com.increff.pos.pojo.BrandPojo;
import org.springframework.stereotype.Repository;

import javax.persistence.TypedQuery;

@Repository
public class BrandDao extends AbstractDao<BrandPojo> {

    private static final String SELECT_BY_NAME_AND_CATEGORY = "select b from BrandPojo b where name=:name and category=:category";

    public BrandPojo findByNameAndCategory(String name, String category) {
        TypedQuery<BrandPojo> query = getQuery(SELECT_BY_NAME_AND_CATEGORY);
        query.setParameter("name", name);
        query.setParameter("category", category);
        return getSingle(query);
    }

}
