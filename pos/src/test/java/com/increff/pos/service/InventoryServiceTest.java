package com.increff.pos.service;

import com.increff.pos.AbstractUnitTest;
import com.increff.pos.TestHelper;
import com.increff.pos.dao.InventoryDao;
import com.increff.pos.model.ApiException;
import com.increff.pos.pojo.Inventory;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.junit.Assert.*;

public class InventoryServiceTest extends AbstractUnitTest {
    @Autowired
    private InventoryService inventoryService;
    @Autowired
    private InventoryDao inventoryDao;
    @Autowired
    private TestHelper testHelper;

    @Test
    public void testIncreaseInventory() {
        Inventory dummyInventory = inventoryDao.getById(1L);
        assertNull(dummyInventory);
        inventoryService.increaseInventory(1L, 10L);
        Inventory savedInventory = inventoryDao.getById(1L);
        assertNotNull(savedInventory);
        assertEquals(Long.valueOf(10), savedInventory.getQuantity());
    }

    @Test
    public void testGetById() {
        Inventory inventory = inventoryService.getById(10L);
        assertNotNull(inventory);
        assertEquals(Long.valueOf(0L), inventory.getQuantity());
        testHelper.createInventory(10L, 120L);
        Inventory savedInventory = inventoryService.getById(10L);
        assertNotNull(savedInventory);
        assertEquals(Long.valueOf(120L), savedInventory.getQuantity());
    }

    @Test
    public void testReduceInventory() throws ApiException {
        testHelper.createInventory(1L, 20L);
        testHelper.createInventory(2L, 25L);
        inventoryService.reduceInventory(1L, 15L);
        Inventory inventory = inventoryDao.getById(1L);
        Inventory inventory1 = inventoryDao.getById(2L);
        assertEquals(Long.valueOf(5L), inventory.getQuantity());
        assertEquals(Long.valueOf(25L), inventory1.getQuantity());
    }

    @Test(expected = ApiException.class)
    public void testReduceInventoryBelowZero() throws ApiException {
        testHelper.createInventory(1L, 20L);
        inventoryService.reduceInventory(1L, 21L);
    }

    @Test(expected = ApiException.class)
    public void testReduceDummyInventory() throws ApiException {
        inventoryService.reduceInventory(10L, 1L);
    }


}
