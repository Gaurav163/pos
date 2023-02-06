package com.increff.pos.dao;

import com.increff.pos.pojo.OrderItem;
import org.springframework.stereotype.Repository;

@Repository
public class OrderItemDao extends AbstractDao<OrderItem> {
    public OrderItemDao() {
        super(OrderItem.class);
    }
}
