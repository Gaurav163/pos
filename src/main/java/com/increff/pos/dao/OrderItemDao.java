package com.increff.pos.dao;

import com.increff.pos.pojo.OrderItemPojo;
import org.springframework.stereotype.Repository;

@Repository
public class OrderItemDao extends AbstractDao<OrderItemPojo> {
    public OrderItemDao() {
        super(OrderItemPojo.class);
    }
}
