package com.increff.pos.dao;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.transaction.Transactional;
import java.util.List;

public abstract class Abstract<T> {
    protected Class<T> clazz;
    @PersistenceContext
    private EntityManager em;

    protected T getSingle(TypedQuery<T> query) {
        try {
            return query.getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }

    protected TypedQuery<T> getQuery(String jpql) {
        return em.createQuery(jpql, clazz);
    }

    @Transactional
    public T create(T pojo) {
        em.persist(pojo);
        return pojo;
    }

    public List<T> findAll() {
        String queryString = String.format("select p from %s p", this.clazz.getName());
        System.out.println(queryString);
        TypedQuery<T> query = getQuery(queryString);
        return query.getResultList();
    }

    public T findById(Long id) {
        String queryString = String.format("select p from %s p where id=:id ", this.clazz.getName());
        TypedQuery<T> query = getQuery(queryString);
        query.setParameter("id", id);
        return getSingle(query);
    }

    public boolean check(Long id) {
        return findById(id) != null;
    }

    @Transactional
    public T update(Long id, T pojo) {
        if (!check(id)) {
            throw new IllegalStateException("Item not exist");
        }
        return em.merge(pojo);
    }

    protected EntityManager em() {
        return em;
    }


}
