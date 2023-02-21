package com.increff.pos.dto;

import com.increff.pos.AbstractUnitTest;
import com.increff.pos.Helper;
import com.increff.pos.dao.InventoryDao;
import com.increff.pos.dao.OrderDao;
import com.increff.pos.dao.OrderItemDao;
import com.increff.pos.model.ApiException;
import com.increff.pos.model.OrderData;
import com.increff.pos.model.OrderForm;
import com.increff.pos.model.OrderItemForm;
import com.increff.pos.pojo.Inventory;
import com.increff.pos.pojo.Order;
import com.increff.pos.pojo.OrderItem;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class OrderDtoTest extends AbstractUnitTest {
    @Autowired
    private OrderDto orderDto;
    @Autowired
    private OrderDao orderDao;
    @Autowired
    private OrderItemDao orderItemDao;
    @Autowired
    private InventoryDao inventoryDao;
    @Autowired
    private Helper helper;
    @Value("${invoicesPath}")
    private String basePath;

    @Test
    public void testCreate() throws ApiException {
        Long productId1 = helper.initOrderItem("barcode1", 10L);
        Long productId2 = helper.initOrderItem("barcode2", 20L);
        Long productId3 = helper.initOrderItem("barcode3", 30L);
        List<OrderItemForm> itemList = new ArrayList<>();
        itemList.add(helper.createOrderItemForm("barcode1", 5L, 99.9));
        itemList.add(helper.createOrderItemForm("barcode2", 20L, 99.9));
        itemList.add(helper.createOrderItemForm("barcode3", 7L, 99.9));
        OrderForm orderForm = new OrderForm();
        orderForm.setItems(itemList);
        OrderData orderData = orderDto.create(orderForm);
        assertNotNull(orderData);
        assertEquals(3, orderData.getItems().size());
        Order order = orderDao.getById(orderData.getId());
        assertNotNull(order);
        List<OrderItem> orderItems = orderItemDao.getListByParameter("orderId", order.getId());
        assertEquals(3, orderItems.size());
        Inventory inv1 = inventoryDao.getById(productId1);
        Inventory inv2 = inventoryDao.getById(productId2);
        Inventory inv3 = inventoryDao.getById(productId3);
        assertEquals(Long.valueOf(5), inv1.getQuantity());
        assertEquals(Long.valueOf(0), inv2.getQuantity());
        assertEquals(Long.valueOf(23), inv3.getQuantity());
    }

    @Test(expected = ApiException.class)
    public void testCreateLowInventory() throws ApiException {
        Long productId1 = helper.initOrderItem("barcode1", 10L);
        Long productId2 = helper.initOrderItem("barcode2", 20L);
        List<OrderItemForm> itemList = new ArrayList<>();
        itemList.add(helper.createOrderItemForm("barcode1", 5L, 99.9));
        itemList.add(helper.createOrderItemForm("barcode2", 25L, 99.9));
        OrderForm orderForm = new OrderForm();
        orderForm.setItems(itemList);
        OrderData orderData = orderDto.create(orderForm);
    }

    @Test(expected = ApiException.class)
    public void testCreateWrongBarcode() throws ApiException {
        Long productId1 = helper.initOrderItem("barcode1", 10L);
        List<OrderItemForm> itemList = new ArrayList<>();
        itemList.add(helper.createOrderItemForm("barcode1", 5L, 99.9));
        itemList.add(helper.createOrderItemForm("barcode2", 20L, 99.9));
        OrderForm orderForm = new OrderForm();
        orderForm.setItems(itemList);
        OrderData orderData = orderDto.create(orderForm);
    }

    @Test
    public void testGetAll() throws ApiException {
        Order order1 = helper.createOrder();
        Order order2 = helper.createOrder();
        Order order3 = helper.createOrder();
        Order order4 = helper.createOrder();
        Long productId1 = helper.initOrderItem("barcode1", 50L);
        Long productId2 = helper.initOrderItem("barcode2", 60L);
        helper.createOrderItem(order1.getId(), productId1, 5L, 60D);
        helper.createOrderItem(order1.getId(), productId2, 5L, 60D);
        helper.createOrderItem(order2.getId(), productId1, 5L, 60D);
        helper.createOrderItem(order2.getId(), productId2, 5L, 60D);
        helper.createOrderItem(order3.getId(), productId1, 5L, 60D);
        helper.createOrderItem(order3.getId(), productId2, 5L, 60D);
        helper.createOrderItem(order4.getId(), productId1, 5L, 60D);
        helper.createOrderItem(order4.getId(), productId2, 5L, 60D);
        List<OrderData> orderList = orderDto.getAll();
        assertEquals(4, orderList.size());
    }

    @Test
    public void testGetById() throws ApiException {
        Order order1 = helper.createOrder();
        Long productId1 = helper.initOrderItem("barcode1", 50L);
        Long productId2 = helper.initOrderItem("barcode2", 60L);
        helper.createOrderItem(order1.getId(), productId1, 5L, 60D);
        helper.createOrderItem(order1.getId(), productId2, 5L, 60D);
        OrderData savedOrder = orderDto.getById(order1.getId());
        OrderData dummyOrder = orderDto.getById(order1.getId() + 5);
        assertNotNull(savedOrder);
        assertNull(dummyOrder);
    }

    @Test
    public void testGenerateInvoice() throws ApiException {
        Order order1 = helper.createOrder();
        Long productId1 = helper.initOrderItem("barcode1", 50L);
        Long productId2 = helper.initOrderItem("barcode2", 60L);
        Long productId3 = helper.initOrderItem("barcode3", 60L);
        helper.createOrderItem(order1.getId(), productId1, 5L, 60D);
        helper.createOrderItem(order1.getId(), productId2, 5L, 70D);
        helper.createOrderItem(order1.getId(), productId3, 5L, 80D);
        orderDto.generateInvoice(order1.getId());
        Order order = orderDao.getById(order1.getId());
        assertTrue(order.getInvoiced());
        File file = new File(basePath + "invoice-" + order.getId() + ".pdf");
        assertTrue(file.exists());
        String invoice = orderDto.getInvoiceAsBase64(order.getId());
        assertNotNull(invoice);
    }

    @Test(expected = ApiException.class)
    public void testGenerateInvoiceInvalidOrder() throws ApiException {
        Order order1 = helper.createOrder();
        orderDto.generateInvoice(order1.getId() + 100L);
    }

    @Test(expected = ApiException.class)
    public void testGetInvalidInvoice() throws ApiException {
        Order order1 = helper.createOrder();
        orderDto.getInvoiceAsBase64(order1.getId() + 10000000L);
    }


}
