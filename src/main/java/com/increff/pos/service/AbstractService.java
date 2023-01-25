package com.increff.pos.service;

import com.increff.pos.dao.AbstractDao;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public abstract class AbstractService<T> {
    
    protected AbstractDao<T> dao;

    public T getById(Long id) {
        return dao.findById(id);
    }

    public List<T> getAll() {
        return dao.findAll();
    }

    public T getOneByParameter(String name, String value) {
        return dao.findOneByMember(name, value);
    }

    public List<T> getListByParameter(String name, String value) {
        return dao.findListByMember(name, value);
    }


}
