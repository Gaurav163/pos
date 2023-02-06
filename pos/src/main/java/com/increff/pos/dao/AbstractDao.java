package com.increff.pos.dao;

import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.util.List;

@Repository
public abstract class AbstractDao<T> {
    private final Class<T> clazz;

    @PersistenceContext
    protected EntityManager em;

    protected AbstractDao(Class<T> clazz) {
        this.clazz = clazz;
    }

    public T create(T pojo) {
        em.persist(pojo);
        return pojo;
    }

    public List<T> getAll() {
        String queryString = String.format("select p from %s p", this.clazz.getSimpleName());
        TypedQuery<T> query = getQuery(queryString);
        return query.getResultList();
    }

    public <S> T getById(S id) {
        String queryString = String.format("select p from %s p where id=:id ", this.clazz.getSimpleName());
        TypedQuery<T> query = getQuery(queryString);
        query.setParameter("id", id);
        return getSingle(query);
    }

    public <S> T getOneByMember(String name, S value) {
        String queryString = String.format("select p from %s p where %s=:value", this.clazz.getSimpleName(), name);
        TypedQuery<T> query = getQuery(queryString);
        query.setParameter("value", value);
        return getSingle(query);
    }

    public <S> List<T> getListByMember(String name, S value) {
        String queryString = String.format("select p from %s p where %s=:value", this.clazz.getSimpleName(), name);
        TypedQuery<T> query = getQuery(queryString);
        query.setParameter("value", value);
        return query.getResultList();
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
