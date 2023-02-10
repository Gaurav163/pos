package com.increff.pos.service;

import com.increff.pos.dao.OrderDao;
import com.increff.pos.model.ApiException;
import com.increff.pos.pojo.Order;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.ZonedDateTime;
import java.util.List;

@Service
@Transactional(rollbackFor = ApiException.class)
public class OrderService {
    @Autowired
    private OrderDao orderDao;

    public Order getById(Long id) {
        return orderDao.getById(id);
    }

    public List<Order> getAll() {
        return orderDao.getAll();
    }

    public Order create() {
        Order order = new Order();
        order.setDatetime(ZonedDateTime.now());
        order.setInvoiced(false);
        orderDao.create(order);
        return order;
    }

    public List<Order> getByDatetimeRange(ZonedDateTime startTime, ZonedDateTime endTime) {
        return orderDao.getByDatetimeRange(startTime, endTime);
    }

    public Order createInvoice(Long id) throws ApiException {
        Order order = orderDao.getById(id);
        if (order == null) {
            throw new ApiException("Invalid order ID");
        } else order.setInvoiced(true);
        return order;
    }
}
