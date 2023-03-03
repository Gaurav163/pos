package com.increff.pos.service;

import com.increff.pos.AbstractUnitTest;
import com.increff.pos.TestHelper;
import com.increff.pos.dao.OrderDao;
import com.increff.pos.model.ApiException;
import com.increff.pos.pojo.Order;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.ZonedDateTime;

import static org.junit.Assert.*;

public class OrderServiceTest extends AbstractUnitTest {
    @Autowired
    private OrderService orderService;
    @Autowired
    private OrderDao orderDao;
    @Autowired
    private TestHelper testHelper;

    @Test
    public void testCreate() {
        Order order = orderService.create();
        Order savedOrder = orderDao.getByParameter("datetime", order.getDatetime());
        assertNotNull(savedOrder);
        assertEquals(order.getId(), savedOrder.getId());
        assertEquals(false, savedOrder.getInvoiced());
    }

    @Test
    public void testCreateInvoice() throws ApiException {
        ZonedDateTime currentTime = ZonedDateTime.now();
        Order order = testHelper.createOrder(currentTime);
        orderService.createInvoice(order.getId());
        Order savedOrder = orderDao.getByParameter("datetime", currentTime);
        assertNotNull(savedOrder);
        assertTrue(savedOrder.getInvoiced());
    }

    @Test(expected = ApiException.class)
    public void testCreateInvoiceInvalidId() throws ApiException {
        orderService.createInvoice(10L);
    }

}
