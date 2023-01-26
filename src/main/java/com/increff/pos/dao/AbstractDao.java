package com.increff.pos.dao;

import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.transaction.Transactional;
import java.lang.reflect.ParameterizedType;
import java.util.List;

@Repository
public abstract class AbstractDao<T> {
    private final Class clazz;

    @PersistenceContext
    protected EntityManager em;

    protected AbstractDao() {
        this.clazz = (Class) ((ParameterizedType) this.getClass().getGenericSuperclass()).getActualTypeArguments()[0];
    }

    @Transactional
    public T create(T pojo) {
        em.persist(pojo);
        return pojo;
    }

    public List<T> findAll() {
        String queryString = String.format("select p from %s p", this.clazz.getSimpleName());
        TypedQuery<T> query = getQuery(queryString);
        return query.getResultList();
    }

    public T findById(Long id) {
        String queryString = String.format("select p from %s p where id=:id ", this.clazz.getSimpleName());
        TypedQuery<T> query = getQuery(queryString);
        query.setParameter("id", id);
        return getSingle(query);
    }

    public T findOneByMember(String name, String value) {
        String queryString = String.format("select p from %s p where %s=:value", this.clazz.getSimpleName(), name);
        TypedQuery<T> query = getQuery(queryString);
        query.setParameter("value", value);
        return getSingle(query);
    }

    public List<T> findListByMember(String name, String value) {
        String queryString = String.format("select p from %s p where %s=:value", this.clazz.getSimpleName(), name);
        TypedQuery<T> query = getQuery(queryString);
        query.setParameter("value", value);
        return query.getResultList();
    }

    public boolean check(Long id) {
        return findById(id) != null;
    }


    protected EntityManager em() {
        return em;
    }

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


}
