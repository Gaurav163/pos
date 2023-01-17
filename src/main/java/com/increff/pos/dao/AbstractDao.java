package com.increff.pos.dao;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

public abstract class AbstractDao {

    @PersistenceContext
    private EntityManager em;


    protected <T> T getSingle(TypedQuery<T> query) {
        try {
            return query.getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }

    protected <T> TypedQuery<T> getQuery(String jpql, Class<T> clazz) {
        return em.createQuery(jpql, clazz);
    }

    protected EntityManager em() {
        return em;
    }


}