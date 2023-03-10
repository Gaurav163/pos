package com.increff.pos.service;

import com.increff.pos.dao.OrderItemDao;
import com.increff.pos.pojo.OrderItem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(rollbackFor = Exception.class)
public class OrderItemService {
    @Autowired
    private OrderItemDao orderItemDao;

    public <S> List<OrderItem> getListByParameter(String name, S value) {
        return orderItemDao.getListByParameter(name, value);
    }

    public void create(OrderItem orderItem) {
        orderItemDao.create(orderItem);
    }
}
