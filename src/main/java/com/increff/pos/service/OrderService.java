package com.increff.pos.service;

import com.increff.pos.dao.OrderDao;
import com.increff.pos.model.ApiException;
import com.increff.pos.pojo.OrderPojo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.ZonedDateTime;
import java.util.Date;
import java.util.List;

@Service
@Transactional(rollbackFor = ApiException.class)
public class OrderService {
    @Autowired
    private OrderDao orderDao;

    public OrderPojo getById(Long id) {
        return orderDao.getById(id);
    }

    public List<OrderPojo> getAll() {
        return orderDao.getAll();
    }

    public OrderPojo create() {
        OrderPojo order = new OrderPojo();
        order.setDatetime(ZonedDateTime.now());
        orderDao.create(order);
        return order;
    }

    public List<OrderPojo> getByDatetimeRange(Date startTime, Date endTime) {
        return orderDao.findByDatetimeRange(startTime, endTime);
    }
}
