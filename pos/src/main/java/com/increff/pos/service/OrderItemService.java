package com.increff.pos.service;

import com.increff.pos.dao.OrderItemDao;
import com.increff.pos.model.ApiException;
import com.increff.pos.pojo.OrderItem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(rollbackFor = ApiException.class)
public class OrderItemService {
    @Autowired
    private OrderItemDao orderItemDao;

    public OrderItem getById(Long id) {
        return orderItemDao.getById(id);
    }

    public List<OrderItem> getAll() {
        return orderItemDao.getAll();
    }

    public <S> List<OrderItem> getListByParameter(String name, S value) {
        return orderItemDao.getListByMember(name, value);
    }

    public void create(OrderItem orderItem) {
        orderItemDao.create(orderItem);
    }
}
