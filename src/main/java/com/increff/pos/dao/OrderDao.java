package com.increff.pos.dao;

import com.increff.pos.pojo.OrderPojo;
import org.springframework.stereotype.Repository;

import javax.persistence.TypedQuery;
import java.time.ZonedDateTime;
import java.util.List;

@Repository
public class OrderDao extends AbstractDao<OrderPojo> {

    public OrderDao() {
        super(OrderPojo.class);
    }

    private static final String SELECT_BY_DATETIME_RANGE = "select b from OrderPojo b where datetime>=:startTime and datetime<=:endTime";

    public List<OrderPojo> findByDatetimeRange(ZonedDateTime startTime, ZonedDateTime endTime) {
        TypedQuery<OrderPojo> query = getQuery(SELECT_BY_DATETIME_RANGE);
        query.setParameter("startTime", startTime);
        query.setParameter("endTime", endTime);
        return query.getResultList();
    }
}
