package com.increff.pos.service;

import com.increff.pos.AbstractUnitTest;
import com.increff.pos.TestHelper;
import com.increff.pos.dao.OrderItemDao;
import com.increff.pos.pojo.OrderItem;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.junit.Assert.assertNotNull;

public class OrderItemServiceTest extends AbstractUnitTest {
    @Autowired
    private OrderItemService orderItemService;
    @Autowired
    private OrderItemDao orderItemDao;
    @Autowired
    private TestHelper testHelper;

    @Test
    public void testCreate() {
        OrderItem orderItem1 = testHelper.getOrderItem(1L, 2L, 5L, 90.90);
        orderItemService.create(orderItem1);
        OrderItem savedItem = orderItemDao.getByParameter("orderId", 1L);
        assertNotNull(savedItem);
    }
}
