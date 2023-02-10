package com.increff.pos.dao;

import com.increff.pos.AbstractUnitTest;
import com.increff.pos.Helper;
import com.increff.pos.pojo.OrderItem;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static org.junit.Assert.*;

public class OrderItemDaoTest extends AbstractUnitTest {
    @Autowired
    private OrderItemDao orderItemDao;
    @Autowired
    private Helper helper;

    @Test
    public void testCreate() {
        OrderItem item = helper.getOrderItem(1L, 1L, 10L, 90.50);
        orderItemDao.create(item);
        OrderItem savedItem = orderItemDao.getByParameter("orderId", 1L);
        assertNotNull(savedItem);
        assertEquals(item.getQuantity(), savedItem.getQuantity());
        assertEquals(item.getProductId(), savedItem.getProductId());
        assertEquals(item.getSellingPrice(), savedItem.getSellingPrice());
    }

    @Test
    public void testGetById() {
        OrderItem item = helper.createOrderItem(1L, 3L, 20L, 999.9);
        OrderItem savedItem = orderItemDao.getById(item.getId());
        assertNotNull(savedItem);
        assertEquals(item.getProductId(), savedItem.getProductId());
        OrderItem dummyItem = orderItemDao.getById(item.getId() + 5);
        assertNull(dummyItem);
    }

    @Test
    public void testGetAll() {
        helper.createOrderItem(1L, 3L, 20L, 999.9);
        helper.createOrderItem(1L, 4L, 20L, 999.9);
        helper.createOrderItem(2L, 10L, 10L, 999.9);
        helper.createOrderItem(3L, 3L, 20L, 999.9);
        List<OrderItem> items = orderItemDao.getAll();
        assertEquals(4L, items.size());
    }

    @Test
    public void testGetByParameter() {
        helper.createOrderItem(1L, 3L, 20L, 999.9);
        helper.createOrderItem(1L, 4L, 20L, 999.9);
        helper.createOrderItem(2L, 3L, 10L, 999.9);
        helper.createOrderItem(3L, 3L, 20L, 999.9);
        helper.createOrderItem(3L, 9L, 20L, 999.9);
        helper.createOrderItem(3L, 4L, 20L, 999.9);
        List<OrderItem> itemList1 = orderItemDao.getListByParameter("orderId", 1L);
        List<OrderItem> itemList2 = orderItemDao.getListByParameter("orderId", 3L);
        List<OrderItem> itemList3 = orderItemDao.getListByParameter("productId", 3L);
        List<OrderItem> itemList4 = orderItemDao.getListByParameter("productId", 9L);
        assertEquals(2, itemList1.size());
        assertEquals(3, itemList2.size());
        assertEquals(3, itemList3.size());
        assertEquals(1, itemList4.size());
    }
}
