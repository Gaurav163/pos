package com.increff.pos.dao;

import com.increff.pos.pojo.BrandPojo;
import org.springframework.stereotype.Repository;

import javax.persistence.*;
import javax.transaction.Transactional;
import java.util.List;

@Repository
public class BrandDao extends AbstractDao {

    private static String select_id = "select b from BrandPojo b where id=:id";
    private static String select_all = "select b from BrandPojo b";
    private static String select_by_name = "select b from BrandPojo b where name=:name";
    private static String select_by_category = "select b from BrandPojo b where category=:category";
    private static String select_by_name_and_category = "select b from BrandPojo b where name=:name and category=:category";


    @PersistenceContext
    private EntityManager em;

    @Transactional
    public BrandPojo save(BrandPojo b) {
        em.persist(b);
        return b;
    }

    public BrandPojo findById(Long id) {
        TypedQuery<BrandPojo> query = getQuery(select_id, BrandPojo.class);
        query.setParameter("id", id);
        try {
            return query.getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }

    public List<BrandPojo> findAll() {
        TypedQuery<BrandPojo> query = getQuery(select_all, BrandPojo.class);
        return query.getResultList();
    }

    public List<BrandPojo> findByName(String name) {
        TypedQuery<BrandPojo> query = getQuery(select_by_name, BrandPojo.class);
        query.setParameter("name", name);
        return query.getResultList();
    }

    public List<BrandPojo> findByCategory(String category) {
        TypedQuery<BrandPojo> query = getQuery(select_by_category, BrandPojo.class);
        query.setParameter("category", category);
        return query.getResultList();
    }

    public BrandPojo findByNameAndCategory(String name, String category) {
        TypedQuery<BrandPojo> query = getQuery(select_by_name_and_category, BrandPojo.class);
        query.setParameter("name", name);
        query.setParameter("category", category);
        try {
            return query.getSingleResult();
        } catch (NoResultException e) {
            return null;
        }

    }


}
