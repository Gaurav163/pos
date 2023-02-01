package com.increff.pos.service;

import com.increff.pos.dao.OrderItemDao;
import com.increff.pos.model.ApiException;
import com.increff.pos.pojo.OrderItemPojo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(rollbackFor = ApiException.class)
public class OrderItemService {
    @Autowired
    private OrderItemDao orderItemDao;

    public OrderItemPojo getById(Long id) {
        return orderItemDao.getById(id);
    }

    public List<OrderItemPojo> getAll() {
        return orderItemDao.getAll();
    }

    public OrderItemPojo getOneByParameter(String name, String value) {
        return orderItemDao.getOneByMember(name, value);
    }

    public <S> List<OrderItemPojo> getListByParameter(String name, S value) {
        return orderItemDao.getListByMember(name, value);
    }


    public void create(OrderItemPojo pojo) {
        orderItemDao.create(pojo);
    }
}
