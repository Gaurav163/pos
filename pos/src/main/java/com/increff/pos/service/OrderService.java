package com.increff.pos.service;

import com.increff.pos.dao.OrderDao;
import com.increff.pos.model.ApiException;
import com.increff.pos.pojo.Order;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Service
@Transactional(rollbackFor = Exception.class)
public class OrderService {
    @Autowired
    private OrderDao orderDao;

    public Order getById(Long id) {
        return orderDao.getById(id);
    }

    public Order create() {
        Order order = new Order();
        order.setDatetime(ZonedDateTime.now().truncatedTo(ChronoUnit.SECONDS));
        order.setInvoiced(false);
        orderDao.create(order);
        return order;
    }

    public List<Order> getByDatetimeRange(ZonedDateTime startTime, ZonedDateTime endTime) {
        return orderDao.getByDatetimeRange(startTime, endTime);
    }

    public List<Order> getBySizeAndPage(Long size, Long lastId) {
        if (lastId == null) lastId = Long.MAX_VALUE;
        if (size == null) size = 100L;
        return orderDao.getBySizeAndPage(size, lastId);
    }

    public Order createInvoice(Long id) throws ApiException {
        Order order = orderDao.getById(id);
        if (order == null) {
            throw new ApiException("Invalid order ID");
        } else order.setInvoiced(true);
        return order;
    }

    public List<Order> getNextBatch(Integer batchSize, Long startId) {
        return orderDao.getNextBatch(batchSize, startId);
    }
}
