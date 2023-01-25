package com.increff.pos.service;

import com.increff.pos.dao.OrderDao;
import com.increff.pos.pojo.OrderPojo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class OrderService extends AbstractService<OrderPojo> {

    private final OrderDao orderDao;

    @Autowired
    public OrderService(OrderDao orderDao) {
        this.orderDao = orderDao;
        this.dao = orderDao;
    }

    public OrderPojo create() {
        OrderPojo order = new OrderPojo();
        order.setDatetime(new Date());
        order = orderDao.create(order);
        return order;
    }

    public List<OrderPojo> getByDatetimeRange(Date startTime, Date endTime) {
        return orderDao.findByDatetimeRange(startTime, endTime);
    }
}
