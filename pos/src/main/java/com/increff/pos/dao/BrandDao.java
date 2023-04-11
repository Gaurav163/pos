package com.increff.pos.dao;

import com.increff.pos.pojo.Brand;
import org.springframework.stereotype.Repository;

import javax.persistence.TypedQuery;
import java.util.List;

@Repository
public class BrandDao extends AbstractDao<Brand> {

    private static final String SELECT_BY_NAME_AND_CATEGORY = "select b from Brand b where name=:name and category=:category";
    private static final String SELECT_BY_ID_LIST = "select b from Brand b where id in :id_list";
    public BrandDao() {
        super(Brand.class);
    }

    public Brand getByNameAndCategory(String name, String category) {
        TypedQuery<Brand> query = getQuery(SELECT_BY_NAME_AND_CATEGORY);
        query.setParameter("name", name);
        query.setParameter("category", category);
        return getSingle(query);
    }

    public List<Brand> getListByIdList(List<Long> idList) {
        TypedQuery<Brand> query = getQuery(SELECT_BY_ID_LIST);
        query.setParameter("id_list", idList);
        return query.getResultList();
    }
}
