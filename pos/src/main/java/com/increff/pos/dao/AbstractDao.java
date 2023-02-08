package com.increff.pos.dao;

import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
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

    public <S> T getByParameter(String name, S value) {
        String queryString = String.format("select p from %s p where %s=:value", this.clazz.getSimpleName(), name);
        TypedQuery<T> query = getQuery(queryString);
        query.setParameter("value", value);
        return getSingle(query);
    }

    public <S> List<T> getListByParameter(String name, S value) {
        String queryString = String.format("select p from %s p where %s=:value", this.clazz.getSimpleName(), name);
        TypedQuery<T> query = getQuery(queryString);
        query.setParameter("value", value);
        return query.getResultList();
    }


    protected T getSingle(TypedQuery<T> query) {
        return query.getResultList().stream().findFirst().orElse(null);
    }

    protected TypedQuery<T> getQuery(String jpql) {
        return em.createQuery(jpql, clazz);
    }


}
