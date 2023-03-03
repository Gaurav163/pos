package com.increff.pos.dao;

import com.increff.pos.AbstractUnitTest;
import com.increff.pos.TestHelper;
import com.increff.pos.pojo.Inventory;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static org.junit.Assert.*;

public class InventoryDaoTest extends AbstractUnitTest {

    @Autowired
    private InventoryDao inventoryDao;
    @Autowired
    private TestHelper testHelper;

    @Test
    public void testCreate() {
        Inventory inventory = testHelper.getInventory(1L, 10L);
        inventoryDao.create(inventory);
        Inventory savedInv = inventoryDao.getById(1L);
        assertNotNull(savedInv);
        assertEquals(inventory.getQuantity(), savedInv.getQuantity());
    }

    @Test
    public void testGetAll() {
        testHelper.createInventory(1L, 10L);
        testHelper.createInventory(2L, 20L);
        testHelper.createInventory(3L, 30L);
        testHelper.createInventory(4L, 40L);
        List<Inventory> inventories = inventoryDao.getAll();
        assertEquals(4, inventories.size());
    }

    @Test
    public void testGetById() {
        Inventory inventory1 = testHelper.createInventory(2L, 20L);
        Inventory savedInv = inventoryDao.getById(2L);
        Inventory dummuyInv = inventoryDao.getById(4L);
        assertNotNull(savedInv);
        assertEquals(Long.valueOf(20L), savedInv.getQuantity());
        assertNull(dummuyInv);
    }
}
