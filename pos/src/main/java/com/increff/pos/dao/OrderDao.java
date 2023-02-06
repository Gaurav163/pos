package com.increff.pos.dao;

import com.increff.pos.pojo.Order;
import org.springframework.stereotype.Repository;

import javax.persistence.TypedQuery;
import java.time.ZonedDateTime;
import java.util.List;

@Repository
public class OrderDao extends AbstractDao<Order> {

    public OrderDao() {
        super(Order.class);
    }

    private static final String SELECT_BY_DATETIME_RANGE = "select b from Order b where datetime>=:startTime and datetime<=:endTime";

    public List<Order> findByDatetimeRange(ZonedDateTime startTime, ZonedDateTime endTime) {
        TypedQuery<Order> query = getQuery(SELECT_BY_DATETIME_RANGE);
        query.setParameter("startTime", startTime);
        query.setParameter("endTime", endTime);
        return query.getResultList();
    }
}
