package com.increff.pos.dao;

import com.increff.pos.pojo.BrandPojo;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.transaction.Transactional;
import java.util.List;

@Repository
public class BrandDao extends AbstractDao {

    private static final String SELECT_ID = "select b from BrandPojo b where id=:id";
    private static final String SELECT_ALL = "select b from BrandPojo b";
    private static final String SELECT_BY_NAME = "select b from BrandPojo b where name=:name";
    private static final String SELECT_BY_CATEGORY = "select b from BrandPojo b where category=:category";
    private static final String SELECT_BY_NAME_AND_CATEGORY = "select b from BrandPojo b where name=:name and category=:category";


    @PersistenceContext
    private EntityManager em;

    @Transactional
    public void save(BrandPojo b) {
        em.persist(b);
    }

    public BrandPojo findById(Long id) {
        TypedQuery<BrandPojo> query = getQuery(SELECT_ID, BrandPojo.class);
        query.setParameter("id", id);
        return getSingle(query);
    }

    public List<BrandPojo> findAll() {
        TypedQuery<BrandPojo> query = getQuery(SELECT_ALL, BrandPojo.class);
        return query.getResultList();
    }

    public List<BrandPojo> findByName(String name) {
        TypedQuery<BrandPojo> query = getQuery(SELECT_BY_NAME, BrandPojo.class);
        query.setParameter("name", name);
        return query.getResultList();
    }

    public List<BrandPojo> findByCategory(String category) {
        TypedQuery<BrandPojo> query = getQuery(SELECT_BY_CATEGORY, BrandPojo.class);
        query.setParameter("category", category);
        return query.getResultList();
    }

    public BrandPojo findByNameAndCategory(String name, String category) {

        TypedQuery<BrandPojo> query = getQuery(SELECT_BY_NAME_AND_CATEGORY, BrandPojo.class);
        query.setParameter("name", name);
        query.setParameter("category", category);
        return getSingle(query);

    }


}
