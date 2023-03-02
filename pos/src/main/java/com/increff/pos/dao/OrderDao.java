package com.increff.pos.dao;

import com.increff.pos.pojo.Order;
import org.springframework.stereotype.Repository;

import javax.persistence.TypedQuery;
import java.time.ZonedDateTime;
import java.util.List;

@Repository
public class OrderDao extends AbstractDao<Order> {
    private static final String SELECT_BY_DATETIME_RANGE = "select b from Order b where datetime>=:startTime and datetime<:endTime";
    private static final String SELECT_BY_PAGINATION = "select b from Order b where id<:lastId order by id desc";
    private static final String SELECT_FIRST_ORDER = "select b from Order b order by id asc";


    public OrderDao() {
        super(Order.class);
    }

    public List<Order> getByDatetimeRange(ZonedDateTime startTime, ZonedDateTime endTime) {
        TypedQuery<Order> query = getQuery(SELECT_BY_DATETIME_RANGE);
        query.setParameter("startTime", startTime);
        query.setParameter("endTime", endTime);
        return query.getResultList();
    }

    public List<Order> getBySizeAndPage(Long size, Long lastId) {
        TypedQuery<Order> query = getQuery(SELECT_BY_PAGINATION);
        query.setMaxResults(Math.toIntExact(size));
        query.setParameter("lastId", lastId);
        return query.getResultList();
    }

    public Order getFirstOrder() {
        TypedQuery<Order> query = getQuery(SELECT_FIRST_ORDER);
        query.setMaxResults(Math.toIntExact(1));
        return getSingle(query);
    }
}
