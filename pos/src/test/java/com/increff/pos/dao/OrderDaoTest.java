package com.increff.pos.dao;

import com.increff.pos.AbstractUnitTest;
import com.increff.pos.TestHelper;
import com.increff.pos.pojo.Order;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.ZonedDateTime;
import java.util.List;

import static org.junit.Assert.*;

public class OrderDaoTest extends AbstractUnitTest {

    @Autowired
    private OrderDao orderDao;
    @Autowired
    private TestHelper testHelper;


    @Test
    public void testCreate() {
        ZonedDateTime currentTime = ZonedDateTime.now();
        Order order = testHelper.getOrder(currentTime);
        orderDao.create(order);
        Order savedOrder = orderDao.getAll().get(0);
        assertNotNull(savedOrder);
        assertEquals(currentTime, savedOrder.getDatetime());
    }

    @Test
    public void testGetById() {
        ZonedDateTime currentTime = ZonedDateTime.now();
        Order order = testHelper.createOrder(currentTime);
        Order savedOrder = orderDao.getById(order.getId());
        assertNotNull(order);
        assertEquals(currentTime, savedOrder.getDatetime());
        Order dummyOrder = orderDao.getById(order.getId() + 5);
        assertNull(dummyOrder);
    }

    @Test
    public void testGetAll() {
        testHelper.createOrder();
        testHelper.createOrder();
        testHelper.createOrder();
        testHelper.createOrder(ZonedDateTime.now().minusDays(5));
        testHelper.createOrder(ZonedDateTime.now().minusDays(4));
        List<Order> orders = orderDao.getAll();
        assertEquals(5L, orders.size());
    }

    @Test
    public void testFindByDatetimeRange() {
        testHelper.createOrder();
        testHelper.createOrder(ZonedDateTime.now().minusDays(2));
        testHelper.createOrder();
        testHelper.createOrder(ZonedDateTime.now().minusDays(4));
        testHelper.createOrder(ZonedDateTime.now().minusDays(5));
        List<Order> orderList1 = orderDao.getByDatetimeRange(ZonedDateTime.now().minusDays(3), ZonedDateTime.now());
        List<Order> orderList2 = orderDao.getByDatetimeRange(ZonedDateTime.now().minusDays(6), ZonedDateTime.now().minusDays(1));
        List<Order> orderList3 = orderDao.getByDatetimeRange(ZonedDateTime.now().minusDays(3), ZonedDateTime.now().minusDays(1));
        List<Order> orderList4 = orderDao.getByDatetimeRange(ZonedDateTime.now().minusDays(6), ZonedDateTime.now());
        assertEquals(3L, orderList1.size());
        assertEquals(3L, orderList2.size());
        assertEquals(1L, orderList3.size());
        assertEquals(5L, orderList4.size());

    }

}
